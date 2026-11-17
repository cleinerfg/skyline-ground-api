package com.skylineground.modules.identity.user.exception;

import com.skylineground.modules.identity.user.validator.PasswordError;
import com.skylineground.shared.exception.web.support.ProblemDetailFactory;
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
class InvalidPasswordResolverTest {

    @Mock
    private ProblemDetailFactory problemDetailFactory;

    @InjectMocks
    private InvalidPasswordResolver resolver;

    @Test
    void shouldResolveInvalidPasswordException() {
        InvalidPasswordException exception = new InvalidPasswordException(
                Set.of(PasswordError.TOO_SHORT, PasswordError.MUST_HAVE_NUMBER)
        );

        ProblemDetail expected = ProblemDetail.forStatusAndDetail(
                exception.getErrorCode().getStatus(),
                exception.getMessage()
        );

        when(problemDetailFactory.create(
                exception.getErrorCode(),
                exception.getMessage()
        )).thenReturn(expected);

        ProblemDetail result = resolver.resolve(exception);

        assertThat(result).isEqualTo(expected);
        assertThat(result.getProperties())
                .containsEntry("fails", Set.of("TOO_SHORT", "MUST_HAVE_NUMBER"));

        verify(problemDetailFactory).create(
                exception.getErrorCode(),
                exception.getMessage()
        );
    }
}
