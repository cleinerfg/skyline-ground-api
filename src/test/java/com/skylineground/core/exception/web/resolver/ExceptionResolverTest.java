package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import org.junit.jupiter.api.BeforeEach;
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
class ExceptionResolverTest {

    @Mock
    private ProblemDetailFactory problemDetailFactory;

    @InjectMocks
    private ExceptionResolver resolver;

    private Exception inputException;
    private AppException expectedAppException;

    @BeforeEach
    void setUp() {
        inputException = new Exception("Test error message");
        expectedAppException = AppException.systemIntegrity(inputException.getMessage());
    }

    @Test
    void shouldResolveException() {
        ProblemDetail expectedProblemDetail = ProblemDetail.forStatusAndDetail(
                ErrorCode.SYSTEM_INTEGRITY_ERROR.getStatus(),
                expectedAppException.getMessage()
        );

        when(problemDetailFactory.create(
                expectedAppException.getErrorCode(),
                expectedAppException.getMessage()
        )).thenReturn(expectedProblemDetail);

        ProblemDetail result = resolver.resolve(inputException);

        assertThat(result).isEqualTo(expectedProblemDetail);

        verify(problemDetailFactory).create(
                expectedAppException.getErrorCode(),
                expectedAppException.getMessage()
        );
    }
}
