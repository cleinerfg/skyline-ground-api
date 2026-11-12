package com.skylineground.core.exception.web.resolver.handler;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.web.resolver.schema.FieldFormatViolation;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvalidQueryParamsHandlerTest {

    private enum TestStatus {
        ACTIVE,
        INACTIVE
    }

    @Mock
    private ProblemDetailFactory problemDetailFactory;

    @InjectMocks
    private InvalidQueryParamsHandler handler;

    @Test
    void shouldHandleTypeMismatchQueryParam() {
        var exception = AppException.invalidQueryParams();

        var typeMismatch = new TypeMismatchException("invalid", Integer.class);

        var fieldError = new FieldError(
                "request",
                "page",
                "invalid",
                false,
                new String[]{"typeMismatch"},
                null,
                "Invalid value"
        );
        fieldError.wrap(typeMismatch);

        var expected = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        when(problemDetailFactory.create(
                exception.getErrorCode(),
                exception.getMessage()
        )).thenReturn(expected);

        var result = handler.handle(List.of(fieldError));

        assertThat(result).isSameAs(expected);

        var fails = assertThat(result.getProperties())
                .extracting("fails", list(FieldFormatViolation.class))
                .actual();

        assertThat(fails).hasSize(1);

        var violation = fails.getFirst();

        assertThat(violation.field()).isEqualTo("page");
        assertThat(violation.sentValue()).isEqualTo("invalid");
        assertThat(violation.expectedType()).isEqualTo("Integer");
        assertThat(violation.acceptedValues()).isEmpty();
        assertThat(violation.message())
                .isEqualTo("The value must be a valid number.");

        verify(problemDetailFactory).create(
                exception.getErrorCode(),
                exception.getMessage()
        );
    }

    @Test
    void shouldExtractAcceptedEnumValues() {
        var exception = AppException.invalidQueryParams();

        var typeMismatch = new TypeMismatchException("UNKNOWN", TestStatus.class);

        var fieldError = new FieldError(
                "request",
                "status",
                "UNKNOWN",
                false,
                new String[]{"typeMismatch"},
                null,
                "Invalid status"
        );
        fieldError.wrap(typeMismatch);

        var expected = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        when(problemDetailFactory.create(
                exception.getErrorCode(),
                exception.getMessage()
        )).thenReturn(expected);

        var result = handler.handle(List.of(fieldError));

        var fails = assertThat(result.getProperties())
                .extracting("fails", list(FieldFormatViolation.class))
                .actual();

        var violation = fails.getFirst();

        assertThat(violation.expectedType()).isEqualTo("TestStatus");
        assertThat(violation.acceptedValues())
                .containsExactlyInAnyOrder("ACTIVE", "INACTIVE");

        verify(problemDetailFactory).create(
                exception.getErrorCode(),
                exception.getMessage()
        );
    }

    @Test
    void shouldReturnNullWhenNoTypeMismatchErrors() {
        var fieldError = new FieldError(
                "request",
                "name",
                "",
                false,
                new String[]{"NotBlank"},
                null,
                "Name is required"
        );

        var result = handler.handle(List.of(fieldError));

        assertThat(result).isNull();

        verifyNoInteractions(problemDetailFactory);
    }
}
