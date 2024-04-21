package org.ergea.foodapp.service;

import lombok.extern.slf4j.Slf4j;
import org.ergea.foodapp.dto.UserRequest;
import org.ergea.foodapp.dto.UserResponse;
import org.ergea.foodapp.entity.User;
import org.ergea.foodapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    public UserResponse create(UserRequest userRequest) {
        validationService.validate(userRequest);
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmailAddress(userRequest.getEmailAddress());
        user.setPassword(userRequest.getPassword());

        if (userRepository.existsByEmailAddress(userRequest.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }
        userRepository.save(user);

        var response = new UserResponse();
        response.setUsername(user.getUsername());
        response.setEmailAddress(user.getEmailAddress());
        return response;
    }

    public List<UserResponse> findAll() {
        var response = new ArrayList<UserResponse>();
        userRepository.findAll().forEach(user -> {
            log.info("USER : {}", user);
            response.add(new UserResponse(user.getUsername(), user.getEmailAddress()));
        });
        return response;
    }

    public UserResponse update(UUID id, UserRequest request) {
        validationService.validate(request);
        log.info("REQUEST : {}", request);
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID User not found"));

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
        return UserResponse.builder().emailAddress(user.getEmailAddress()).username(user.getUsername()).build();
    }

    public UserResponse delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID User not found"));
        userRepository.delete(user);
        return UserResponse.builder().emailAddress(user.getEmailAddress()).username(user.getUsername()).build();
    }
}
