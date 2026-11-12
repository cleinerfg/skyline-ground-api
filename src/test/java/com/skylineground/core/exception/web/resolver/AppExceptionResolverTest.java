package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ProblemDetail;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppExceptionResolverTest {

    @Mock
    private ProblemDetailFactory problemDetailFactory;

    @InjectMocks
    private AppExceptionResolver resolver;

    @Test
    void shouldResolveAppException() {
        AppException exception = AppException.invalidJson();

        ProblemDetail expected = ProblemDetail.forStatusAndDetail(
                ErrorCode.INVALID_JSON.getStatus(),
                exception.getMessage()
        );

        when(problemDetailFactory.create(
                exception.getErrorCode(),
                exception.getMessage()
        )).thenReturn(expected);

        assertThat(expected).isNotNull();

        ProblemDetail result = resolver.resolve(exception);

        assertThat(result).isEqualTo(expected);
        assertThat(result.getProperties()).isNull();

        verify(problemDetailFactory).create(
                exception.getErrorCode(),
                exception.getMessage()
        );
    }

    @Test
    void shouldIncludeFailsWhenPresent() {
        AppException exception = AppException.validationFail()
                .withFails(Set.of("NAME_IS_REQUIRED", "EMAIL_IS_INVALID"));

        ProblemDetail expected = ProblemDetail.forStatusAndDetail(
                ErrorCode.VALIDATION_FAIL.getStatus(),
                exception.getMessage()
        );

        when(problemDetailFactory.create(
                exception.getErrorCode(),
                exception.getMessage()
        )).thenReturn(expected);

        ProblemDetail result = resolver.resolve(exception);

        assertThat(result.getProperties())
                .containsEntry("fails", exception.getFails());
    }

    @Test
    void shouldNotIncludeFailsWhenEmpty() {
        AppException exception = AppException.validationFail();

        ProblemDetail expected = ProblemDetail.forStatusAndDetail(
                ErrorCode.VALIDATION_FAIL.getStatus(),
                exception.getMessage()
        );

        when(problemDetailFactory.create(
                exception.getErrorCode(),
                exception.getMessage()
        )).thenReturn(expected);

        ProblemDetail result = resolver.resolve(exception);

        assertThat(result.getProperties()).isNull();
    }
}
