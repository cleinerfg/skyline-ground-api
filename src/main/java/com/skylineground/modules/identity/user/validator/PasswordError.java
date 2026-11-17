package com.skylineground.modules.identity.user.validator;

public enum PasswordError {
    TOO_SHORT,
    MUST_HAVE_UPPERCASE,
    MUST_HAVE_LOWERCASE,
    MUST_HAVE_NUMBER,
    MUST_HAVE_SPECIAL_CHARACTER,
    NO_WHITESPACE,
    NO_SEQUENTIAL_CHARACTER,
    NO_REPEATING_CHARACTER
}
