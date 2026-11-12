package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.web.resolver.schema.FieldFormatViolation;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvalidParamResolverTest {

    private enum TestStatus {
        ACTIVE,
        INACTIVE
    }

    @Mock
    private ProblemDetailFactory problemDetailFactory;

    @InjectMocks
    private InvalidParamResolver resolver;

    @Test
    void shouldResolveInvalidParam() {
        var exception = new MethodArgumentTypeMismatchException(
                "invalid",
                Integer.class,
                "age",
                null,
                null
        );

        var expected = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        when(problemDetailFactory.create(
                AppException.invalidParam().getErrorCode(),
                AppException.invalidParam().getMessage()
        )).thenReturn(expected);

        var result = resolver.resolve(exception);

        assertThat(result).isEqualTo(expected);
        assertThat(result.getProperties()).isNotNull();
        assertThat(result.getProperties()).containsKey("fail");

        var violation = (FieldFormatViolation) result.getProperties().get("fail");

        assertThat(violation.field()).isEqualTo("age");
        assertThat(violation.sentValue()).isEqualTo("invalid");
        assertThat(violation.expectedType()).isEqualTo("Integer");
        assertThat(violation.acceptedValues()).isEmpty();
        assertThat(violation.message()).isEqualTo(
                "The value must be a valid number."
        );

        verify(problemDetailFactory).create(
                AppException.invalidParam().getErrorCode(),
                AppException.invalidParam().getMessage()
        );
    }

    @Test
    void shouldResolveInvalidParamWhenRequiredTypeIsNull() {
        var exception = new MethodArgumentTypeMismatchException(
                "invalid",
                null,
                "unknown",
                null,
                null
        );

        var expected = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        when(problemDetailFactory.create(
                AppException.invalidParam().getErrorCode(),
                AppException.invalidParam().getMessage()
        )).thenReturn(expected);

        var result = resolver.resolve(exception);

        Assertions.assertNotNull(result.getProperties());
        var violation = (FieldFormatViolation) result.getProperties().get("fail");

        assertThat(violation.field()).isEqualTo("unknown");
        assertThat(violation.sentValue()).isEqualTo("invalid");
        assertThat(violation.expectedType()).isEqualTo("UNKNOWN");
        assertThat(violation.acceptedValues()).isEmpty();

        verify(problemDetailFactory).create(
                AppException.invalidParam().getErrorCode(),
                AppException.invalidParam().getMessage()
        );
    }

    @Test
    void shouldExtractAcceptedEnumValues() {
        var exception = new MethodArgumentTypeMismatchException(
                "UNKNOWN",
                TestStatus.class,
                "status",
                null,
                null
        );

        var expected = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        when(problemDetailFactory.create(
                AppException.invalidParam().getErrorCode(),
                AppException.invalidParam().getMessage()
        )).thenReturn(expected);

        var result = resolver.resolve(exception);

        Assertions.assertNotNull(result.getProperties());
        var violation = (FieldFormatViolation) result.getProperties().get("fail");

        assertThat(violation.expectedType()).isEqualTo("TestStatus");
        assertThat(violation.acceptedValues())
                .containsExactlyInAnyOrder("ACTIVE", "INACTIVE");
    }
}
