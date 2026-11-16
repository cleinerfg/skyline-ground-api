package com.skylineground.core.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AppExceptionTest {

    @Test
    void shouldCreateExceptionWithErrorCodeAndMessage() {
        AppException exception = new AppException(
                ErrorCode.INVALID_JSON,
                "Invalid JSON"
        );

        assertThat(exception.getErrorCode())
                .isEqualTo(ErrorCode.INVALID_JSON);

        assertThat(exception.getMessage())
                .isEqualTo("Invalid JSON");
    }

    @Test
    void shouldCreateInvalidJsonException() {
        AppException exception = AppException.invalidJson();

        assertThat(exception.getErrorCode())
                .isEqualTo(ErrorCode.INVALID_JSON);
    }

    @Test
    void shouldCreateInvalidParamException() {
        AppException exception = AppException.invalidParam();

        assertThat(exception.getErrorCode())
                .isEqualTo(ErrorCode.INVALID_PARAM);
    }

    @Test
    void shouldCreateInvalidQueryParamsException() {
        AppException exception = AppException.invalidQueryParams();

        assertThat(exception.getErrorCode())
                .isEqualTo(ErrorCode.INVALID_QUERY_PARAMS);
    }

    @Test
    void shouldCreateValidationFailException() {
        AppException exception = AppException.validationFail();

        assertThat(exception.getErrorCode())
                .isEqualTo(ErrorCode.VALIDATION_FAIL);
    }

    @Test
    void shouldCreateResourceNotFoundException() {
        UUID externalId = UUID.randomUUID();
        AppException exception = AppException.resourceNotFound(
                "User",
                externalId
        );

        assertThat(exception.getErrorCode())
                .isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
    }

    @Test
    void shouldCreateInvalidPeriodException() {
        AppException exception = AppException.invalidPeriod(
                "2026-01-01",
                "2025-01-01"
        );

        assertThat(exception.getErrorCode())
                .isEqualTo(ErrorCode.INVALID_PERIOD);
    }

    @Test
    void shouldCreateSystemIntegrityException() {
        AppException exception = AppException.systemIntegrity(
                new Exception()
        );

        assertThat(exception.getErrorCode())
                .isEqualTo(ErrorCode.SYSTEM_INTEGRITY_ERROR);
    }
}
