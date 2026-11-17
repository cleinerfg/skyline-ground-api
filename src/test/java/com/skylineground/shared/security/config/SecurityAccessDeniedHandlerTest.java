package com.skylineground.shared.security.config;

import com.skylineground.shared.security.exception.UnauthorizedAccessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityAccessDeniedHandlerTest {

    @Mock
    private HandlerExceptionResolver resolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;


    private final AccessDeniedException accessDeniedException = new AccessDeniedException("Access denied");

    @InjectMocks
    private SecurityAccessDeniedHandler accessDeniedHandler;


    @Test
    void shouldResolveUnauthorizedAccessException() {
        accessDeniedHandler.handle(request, response, accessDeniedException);

        verify(resolver).resolveException(
                eq(request),
                eq(response),
                isNull(),
                argThat(UnauthorizedAccessException.class::isInstance)
        );
    }

    @Test
    void shouldNotWriteResponseDirectly() {
        accessDeniedHandler.handle(request, response, accessDeniedException);

        verifyNoInteractions(response);
    }
}
