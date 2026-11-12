package com.skylineground.core.exception.web.support;

import com.skylineground.core.exception.ErrorCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemDetailFactoryTest {

    private final ProblemDetailFactory factory = new ProblemDetailFactory();

    /**
     * Runs the same test with multiple input values.
     * EnumSource automatically provides every ErrorCode enum constant,
     * ensuring that all error codes are covered without duplicating test methods.
     */
    @ParameterizedTest
    @EnumSource(ErrorCode.class)
    void shouldCreateProblemDetailFromErrorCode(ErrorCode errorCode) {
        String message = "Test error message";

        ProblemDetail problemDetail = factory.create(errorCode, message);

        assertThat(problemDetail.getStatus())
                .isEqualTo(errorCode.getStatus().value());

        assertThat(problemDetail.getDetail())
                .isEqualTo(message);

        assertThat(problemDetail.getTitle())
                .isEqualTo(errorCode.name());

        assertThat(problemDetail.getProperties())
                .containsEntry("category", errorCode.getCategory());
    }
}
