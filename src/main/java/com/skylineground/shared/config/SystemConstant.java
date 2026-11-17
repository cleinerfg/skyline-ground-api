package com.skylineground.shared.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SystemConstant {

    public static final int JWT_MIN_SECRET_LENGTH = 20;
    public static final Duration JWT_MIN_TOKEN_EXPIRATION = Duration.ofMinutes(5);

    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 64;
}
