package com.skylineground.modules.identity.user;

import com.skylineground.modules.identity.user.dto.UserRequest;
import com.skylineground.modules.identity.user.dto.UserResponse;
import com.skylineground.modules.identity.user.dto.UserSummary;
import com.skylineground.modules.identity.user.persistence.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequest dto) {
        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
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

    public UserSummary toSummary(User entity) {
        return UserSummary.builder()
                .externalId(entity.getExternalId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }
}
