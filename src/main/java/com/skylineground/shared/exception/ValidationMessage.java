package com.skylineground.shared.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationMessage {
    public static final String REQUIRED = "REQUIRED";
    public static final String MAX_LENGTH_255 = "MAX_LENGTH_255";
    public static final String INVALID_EMAIL_FORMAT = "INVALID_EMAIL_FORMAT";
}
