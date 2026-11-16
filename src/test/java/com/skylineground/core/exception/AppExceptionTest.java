package com.skylineground.core.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("exceptionFactoryCases")
    void shouldCreateExceptionWithExpectedErrorCode(
            AppException exception,
            ErrorCode expectedErrorCode
    ) {
        assertThat(exception.getErrorCode())
                .isEqualTo(expectedErrorCode);
    }

    private static Stream<Arguments> exceptionFactoryCases() {
        UUID externalId = UUID.randomUUID();

        return Stream.of(
                Arguments.of(AppException.invalidJson(), ErrorCode.INVALID_JSON),
                Arguments.of(AppException.invalidParam(), ErrorCode.INVALID_PARAM),
                Arguments.of(AppException.invalidQueryParams(), ErrorCode.INVALID_QUERY_PARAMS),
                Arguments.of(AppException.validationFail(), ErrorCode.VALIDATION_FAIL),
                Arguments.of(AppException.resourceNotFound("User", externalId), ErrorCode.RESOURCE_NOT_FOUND),
                Arguments.of(AppException.invalidPeriod("2026-01-01", "2025-01-01"), ErrorCode.INVALID_PERIOD),
                Arguments.of(AppException.systemIntegrity(new Exception()), ErrorCode.SYSTEM_INTEGRITY_ERROR)
        );
    }
}
