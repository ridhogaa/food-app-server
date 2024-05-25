package org.ergea.foodapp.serviceimpl;

import org.ergea.foodapp.config.Config;
import org.ergea.foodapp.config.EmailSender;
import org.ergea.foodapp.config.EmailTemplate;
import org.ergea.foodapp.config.SimpleStringUtils;
import org.ergea.foodapp.dto.auth.*;
import org.ergea.foodapp.entity.User;
import org.ergea.foodapp.entity.oauth2.Role;
import org.ergea.foodapp.mapper.AuthMapper;
import org.ergea.foodapp.repository.UserRepository;
import org.ergea.foodapp.repository.oauth2.RoleRepository;
import org.ergea.foodapp.service.AuthService;
import org.ergea.foodapp.service.ValidationService;
import org.ergea.foodapp.service.oauth.Oauth2UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ValidationService validationService;

    @Value("${BASEURL}")
    private String baseUrl;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private EmailTemplate emailTemplate;

    @Autowired
    private EmailSender emailSender;

    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
    private int expiredToken;

    @Autowired
    private Config config;

    @Autowired
    private Oauth2UserDetailService userDetailsService;

    public User register(RegisterRequest request) {
        validationService.validate(request);
        String[] roleNames = {"ROLE_USER", "ROLE_READ", "ROLE_WRITE"}; // admin

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }

        if (userRepository.existsByEmailAddress(request.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }
        User user = new User();
        user.setUsername(request.getUsername().toLowerCase());
        user.setEmailAddress(request.getEmailAddress());
        String password = encoder.encode(request.getPassword().replaceAll("\\s+", ""));
        List<Role> r = roleRepository.findByNameIn(roleNames);

        user.setRoles(r);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        validationService.validate(request);
        User checkUser = userRepository.findByUsername(request.getUsername());

        if ((checkUser != null) && (encoder.matches(request.getPassword(), checkUser.getPassword()))) {
            if (!checkUser.isEnabled()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not enabled");
            }
        }
        if (checkUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!(encoder.matches(request.getPassword(), checkUser.getPassword()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
        }
        String url = baseUrl + "/oauth/token?username=" + request.getUsername() +
                "&password=" + request.getPassword() +
                "&grant_type=password" +
                "&client_id=my-client-web" +
                "&client_secret=password";
        ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new
                ParameterizedTypeReference<Map>() {
                }
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            User user = userRepository.findByUsername(request.getUsername());
            List<String> roles = new ArrayList<>();

            for (Role role : user.getRoles()) {
                roles.add(role.getName());
            }

            return authMapper.toLoginResponse(response);
        } else {
            throw new ResponseStatusException(response.getStatusCode(), "User not found");
        }
    }

    @Override
    public Object sendEmailOtp(EmailRequest request, String subject) {
        validationService.validate(request);
        String message = "Thanks, please check your email for activation.";

        User found = userRepository.findByEmailAddress(request.getEmailAddress());
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email Not Found");
        }
        String template = subject.equalsIgnoreCase("Register") ? emailTemplate.getRegisterTemplate() : emailTemplate.getResetPassword();
        if (StringUtils.isEmpty(found.getOtp())) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtils.randomString(6, true);
                search = userRepository.findByOtp(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();
            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername()));
            template = template.replaceAll("\\{\\{TOKEN}}", otp);
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername()));
            template = template.replaceAll("\\{\\{TOKEN}}", found.getOtp());
        }
        emailSender.sendAsync(found.getEmailAddress(), subject, template);
        return message;
    }

    @Override
    public Object confirmOtp(String otp) {
        User user = userRepository.findByOtp(otp);
        if (null == user) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OTP Not Found");
        }
        if (user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.OK, "Account Already Active, Please login!");
        }
        String today = config.convertDateToString(new Date());

        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if (Long.parseLong(today) > Long.parseLong(dateToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your token is expired. Please Get token again.");
        }
        user.setEnabled(true);
        userRepository.save(user);

        return "Success, Please login!";
    }

    @Override
    public Object checkOtpValid(OtpRequest otp) {
        validationService.validate(otp);
        if (otp.getOtp() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp is required");

        User user = userRepository.findByOtp(otp.getOtp());
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp Not Valid");

        return "Success, Please Change New Password!";
    }

    @Override
    public Object resetPassword(ResetPasswordRequest request) {
        validationService.validate(request);
        User user = userRepository.findByOtp(request.getOtp());
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp Not Valid");

        user.setPassword(encoder.encode(request.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);

        userRepository.save(user);

        return "Success Reset Password, Please login with your new password!";
    }

    @Override
    public User getCurrentUser(Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName());
            if (user == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
            }
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
    }
}