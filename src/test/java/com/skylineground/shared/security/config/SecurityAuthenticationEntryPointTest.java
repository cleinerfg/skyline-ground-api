package com.skylineground.shared.security.config;

import com.skylineground.shared.security.jwt.TokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityAuthenticationEntryPointTest {

    @Mock
    private HandlerExceptionResolver resolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private final AuthenticationException authException = new BadCredentialsException("Authentication failed");

    @InjectMocks
    private SecurityAuthenticationEntryPoint entryPoint;

    @Test
    void shouldResolveStoredTokenException() {
        TokenException tokenException = TokenException.required();

        when(request.getAttribute(SecurityFilter.AUTH_EXCEPTION_ATTRIBUTE))
                .thenReturn(tokenException);

        entryPoint.commence(request, response, authException);

        verify(resolver).resolveException(
                request, response, null, tokenException
        );
    }

    @Test
    void shouldResolveRequiredTokenExceptionWhenNoExceptionIsStored() {
        when(request.getAttribute(SecurityFilter.AUTH_EXCEPTION_ATTRIBUTE))
                .thenReturn(null);

        entryPoint.commence(request, response, authException);

        verify(resolver).resolveException(
                eq(request),
                eq(response),
                isNull(),
                argThat(TokenException.class::isInstance)
        );
    }

    @Test
    void shouldResolveRequiredTokenExceptionWhenStoredAttributeHasUnexpectedType() {
        when(request.getAttribute(SecurityFilter.AUTH_EXCEPTION_ATTRIBUTE))
                .thenReturn("unexpected value");

        entryPoint.commence(request, response, authException);

        verify(resolver).resolveException(
                eq(request),
                eq(response),
                isNull(),
                argThat(TokenException.class::isInstance)
        );
    }
}
