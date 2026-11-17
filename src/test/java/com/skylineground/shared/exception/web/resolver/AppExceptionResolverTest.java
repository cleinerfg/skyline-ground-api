package com.skylineground.shared.exception.web.resolver;

import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.exception.ErrorCode;
import com.skylineground.shared.exception.web.support.ProblemDetailFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ProblemDetail;

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
}
