package org.ergea.foodapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.ergea.foodapp.dto.UserRequest;
import org.ergea.foodapp.dto.UserResponse;
import org.ergea.foodapp.entity.User;
import org.ergea.foodapp.mapper.UserMapper;
import org.ergea.foodapp.repository.UserRepository;
import org.ergea.foodapp.service.UserService;
import org.ergea.foodapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserResponse create(UserRequest userRequest) {
        validationService.validate(userRequest);
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmailAddress(userRequest.getEmailAddress());
        user.setPassword(userRequest.getPassword());

        if (userRepository.existsByEmailAddress(userRequest.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> findAll() {
        var response = new ArrayList<UserResponse>();
        userRepository.findAll().forEach(user -> {
            log.info("USER : {}", user);
            response.add(userMapper.toUserResponse(user));
        });
        return response;
    }

    @Override
    public UserResponse update(UUID id, UserRequest request) {
        validationService.validate(request);
        log.info("REQUEST : {}", request);
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID User not found"));

        if (userRepository.existsByEmailAddress(request.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }

        if (Objects.nonNull(request.getUsername())) {
            user.setUsername(request.getUsername());
        }

        if (Objects.nonNull(request.getEmailAddress())) {
            user.setEmailAddress(request.getEmailAddress());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(request.getPassword());
        }

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID User not found"));
        userRepository.delete(user);

        return userMapper.toUserResponse(user);
    }

}
