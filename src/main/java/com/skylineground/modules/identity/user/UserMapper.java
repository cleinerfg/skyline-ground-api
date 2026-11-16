package com.skylineground.modules.identity.user;

import com.skylineground.modules.identity.user.dto.UserRequest;
import com.skylineground.modules.identity.user.dto.UserResponse;
import com.skylineground.modules.identity.user.persistence.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {
    public User toEntity(UUID externalId, UserRequest request) {
        return User.builder()
                .externalId(externalId)
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }

    public UserResponse toResponse(User entity) {
        return UserResponse.builder()
                .externalId(entity.getExternalId())
                .name(entity.getName())
                .email(entity.getEmail())
                .systemRole(entity.getSystemRole())
                .build();
    }
}
