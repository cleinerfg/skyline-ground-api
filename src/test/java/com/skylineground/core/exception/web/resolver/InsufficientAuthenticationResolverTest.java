package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import com.skylineground.core.security.jwt.TokenException;
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
class InsufficientAuthenticationResolverTest {

    @Mock
    private ProblemDetailFactory problemDetailFactory;

    @InjectMocks
    private InsufficientAuthenticationResolver resolver;

    @Test
    void shouldResolveInsufficientAuthenticationException() {
        TokenException exception = TokenException.required();

        ProblemDetail expected = ProblemDetail.forStatusAndDetail(
                exception.getErrorCode().getStatus(),
                exception.getMessage()
        );

        when(problemDetailFactory.create(
                exception.getErrorCode(),
                exception.getMessage()
        )).thenReturn(expected);

        ProblemDetail result = resolver.resolve();

        assertThat(result).isEqualTo(expected);

        verify(problemDetailFactory).create(
                exception.getErrorCode(),
                exception.getMessage()
        );
    }
}
