package com.skylineground.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Global
    SYSTEM_INTEGRITY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCategory.INTERNAL_SERVER_ERROR),
    INVALID_JSON(HttpStatus.BAD_REQUEST, ErrorCategory.INVALID_INPUT),
    INVALID_PARAM(HttpStatus.BAD_REQUEST, ErrorCategory.INVALID_INPUT),
    INVALID_QUERY_PARAMS(HttpStatus.BAD_REQUEST, ErrorCategory.INVALID_INPUT),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, ErrorCategory.INVALID_INPUT),
    INVALID_PERIOD(HttpStatus.BAD_REQUEST, ErrorCategory.INVALID_INPUT),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCategory.NOT_FOUND),

    // Routing
    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCategory.ROUTING),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, ErrorCategory.ROUTING),

    // Identity
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, ErrorCategory.BUSINESS_RULE_VIOLATION),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, ErrorCategory.INVALID_INPUT),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, ErrorCategory.BUSINESS_RULE_VIOLATION),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, ErrorCategory.SECURITY),

    // Token
    TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, ErrorCategory.SECURITY),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, ErrorCategory.SECURITY),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, ErrorCategory.SECURITY);

    private final HttpStatus status;
    private final ErrorCategory category;
}
