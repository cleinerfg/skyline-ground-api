package com.skylineground.modules.project.exception;

import com.skylineground.shared.exception.web.support.ProblemDetailFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ProblemDetail;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectAlreadyExistsResolverTest {

    @Mock
    private ProblemDetailFactory problemDetailFactory;

    @InjectMocks
    private ProjectAlreadyExistsResolver resolver;

    @Test
    void shouldResolveProjectAlreadyExistsException() {
        ProjectAlreadyExistsException exception = new ProjectAlreadyExistsException(
                UUID.randomUUID()
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
                .containsEntry("existingProjectId", exception.getExistingProjectId());

        verify(problemDetailFactory).create(
                exception.getErrorCode(),
                exception.getMessage()
        );
    }
}
