package org.ergea.master.service;

import org.ergea.master.dto.UserRequest;
import org.ergea.master.dto.UserResponse;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse create(UserRequest userRequest);

    List<UserResponse> findAll(Pageable pageable, String username, String emailAddress);

    UserResponse update(Principal principal, UserRequest request);

    UserResponse delete(UUID id);

    UserResponse findById(UUID id);
}
