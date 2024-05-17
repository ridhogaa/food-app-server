package org.ergea.foodapp.service;

import org.ergea.foodapp.dto.UserRequest;
import org.ergea.foodapp.dto.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse create(UserRequest userRequest);

    List<UserResponse> findAll(Pageable pageable, String username, String emailAddress);

    UserResponse update(UUID id, UserRequest request);

    UserResponse delete(UUID id);

    UserResponse findById(UUID id);
}
