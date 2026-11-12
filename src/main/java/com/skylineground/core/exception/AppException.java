package com.skylineground.core.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Getter
public class AppException extends RuntimeException {

    private final transient ErrorCode errorCode;
    private final transient Set<String> fails = new LinkedHashSet<>();

    public AppException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException withFails(Set<String> fails) {
        this.fails.addAll(fails);
        return this;
    }

    public static AppException invalidJson() {
        return new AppException(ErrorCode.INVALID_JSON, "The provided JSON is invalid");
    }

    public static AppException invalidParam() {
        return new AppException(ErrorCode.INVALID_PARAM, "The provided param is invalid");
    }

    public static AppException invalidQueryParams() {
        return new AppException(ErrorCode.INVALID_QUERY_PARAMS, "The provided query params is invalid");
    }

    public static AppException validationFail() {
        return new AppException(ErrorCode.VALIDATION_FAIL, "The data provided is invalid.");
    }

    public static AppException resourceNotFound(String resource, UUID externalId) {
        return new AppException(ErrorCode.RESOURCE_NOT_FOUND,
                "The %s with identifier '%s' was not found".formatted(resource, externalId));
    }

    public static AppException invalidPeriod(String startPeriod, String endPeriod) {
        return new AppException(ErrorCode.INVALID_PERIOD,
                "The period from '%s' to '%s' is not valid".formatted(startPeriod, endPeriod));
    }

    public static AppException systemIntegrity(String message) {
        log.error(message);
        return new AppException(ErrorCode.SYSTEM_INTEGRITY_ERROR, "System Integrity Violation");
    }
}
