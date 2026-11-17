package com.skylineground.modules.identity.user.dto;

import lombok.Builder;

@Builder
public record CreateUserRequest(
        String name,
        String email,
        String password
) {
}
