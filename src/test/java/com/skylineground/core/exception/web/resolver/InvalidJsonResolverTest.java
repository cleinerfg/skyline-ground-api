package com.skylineground.core.exception.web.resolver;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;
import com.skylineground.core.exception.web.resolver.schema.FieldFormatViolation;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvalidJsonResolverTest {

    private enum TestStatus {
        ACTIVE,
        INACTIVE
    }

    @Mock
    private ProblemDetailFactory problemDetailFactory;

    @InjectMocks
    private InvalidJsonResolver resolver;

    @Test
    void shouldResolveSyntaxViolation() {
        var exception = new HttpMessageNotReadableException(
                "Malformed JSON",
                new RuntimeException("Unexpected token"),
                mock(HttpInputMessage.class)
        );

        ProblemDetail expected = ProblemDetail.forStatusAndDetail(
                ErrorCode.INVALID_JSON.getStatus(),
                AppException.invalidJson().getMessage()
        );

        when(problemDetailFactory.create(
                ErrorCode.INVALID_JSON,
                AppException.invalidJson().getMessage()
        )).thenReturn(expected);

        ProblemDetail result = resolver.resolve(exception);

        assertThat(result).isEqualTo(expected);

        assertThat(result.getProperties())
                .containsEntry("violation", InvalidJsonResolver.VIOLATION_SYNTAX)
                .doesNotContainKey("fail");

        verify(problemDetailFactory).create(
                ErrorCode.INVALID_JSON,
                AppException.invalidJson().getMessage()
        );
    }

    @Test
    void shouldResolveFormatViolation() {
        InvalidFormatException formatException = InvalidFormatException.from(
                null,
                "Invalid enum value",
                "UNKNOWN",
                TestStatus.class
        );

        formatException.prependPath(Object.class, "status");

        HttpMessageNotReadableException exception =
                new HttpMessageNotReadableException(
                        "Invalid format",
                        formatException,
                        mock(HttpInputMessage.class)
                );

        ProblemDetail expected = ProblemDetail.forStatusAndDetail(
                ErrorCode.INVALID_JSON.getStatus(),
                AppException.invalidJson().getMessage()
        );

        when(problemDetailFactory.create(
                ErrorCode.INVALID_JSON,
                AppException.invalidJson().getMessage()
        )).thenReturn(expected);

        ProblemDetail result = resolver.resolve(exception);

        assertThat(result).isEqualTo(expected);

        assertThat(result.getProperties()).isNotNull();

        assertThat(result.getProperties())
                .containsEntry("violation", InvalidJsonResolver.VIOLATION_FORMAT)
                .containsKey("fail");

        assertThat(result.getProperties().get("fail"))
                .isInstanceOf(FieldFormatViolation.class);

        verify(problemDetailFactory).create(
                ErrorCode.INVALID_JSON,
                AppException.invalidJson().getMessage()
        );
    }
}
