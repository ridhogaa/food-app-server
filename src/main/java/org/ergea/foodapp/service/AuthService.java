package org.ergea.foodapp.service;

import org.ergea.foodapp.dto.auth.LoginRequest;
import org.ergea.foodapp.dto.auth.LoginResponse;
import org.ergea.foodapp.dto.auth.RegisterRequest;
import org.ergea.foodapp.entity.User;
import org.ergea.foodapp.entity.oauth2.Role;
import org.ergea.foodapp.repository.UserRepository;
import org.ergea.foodapp.repository.oauth2.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

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

    public User register(RegisterRequest request) {
        validationService.validate(request);
        String[] roleNames = {"ROLE_USER", "ROLE_READ", "ROLE_WRITE"}; // admin

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }
        User user = new User();
        user.setUsername(request.getUsername().toLowerCase());

        String password = encoder.encode(request.getPassword().replaceAll("\\s+", ""));
        List<Role> r = roleRepository.findByNameIn(roleNames);

        user.setRoles(r);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        validationService.validate(request);
        try {
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

                return LoginResponse.builder()
                        .accessToken(response.getBody().get("access_token"))
                        .tokenType(response.getBody().get("token_type"))
                        .refreshToken(response.getBody().get("refresh_token"))
                        .expiresIn(response.getBody().get("expires_in"))
                        .scope(response.getBody().get("scope"))
                        .jti(response.getBody().get("jti"))
                        .build();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Login");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }
}
