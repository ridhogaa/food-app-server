package org.ergea.foodapp.mapper;


import org.ergea.foodapp.dto.UserResponse;
import org.ergea.foodapp.entity.User;

public final class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .emailAddress(user.getEmailAddress())
                .build();
    }
}
