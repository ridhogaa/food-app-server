package org.ergea.foodapp.mapper;


import org.ergea.foodapp.dto.UserResponse;
import org.ergea.foodapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .emailAddress(user.getEmailAddress())
                .build();
    }
}
