package com.skylineground.core.security.config;

import com.skylineground.core.security.jwt.TokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    // Manual constructor: Lombok does not copy @Qualifier to the generated one.
    public SecurityAuthenticationEntryPoint(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Only reached when an unauthenticated request hits a private route.
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) {
        Object stored = request.getAttribute(SecurityFilter.AUTH_EXCEPTION_ATTRIBUTE);

        // Stored exception = token sent but invalid; none = no token sent.
        TokenException exception = (stored instanceof TokenException tokenException)
                ? tokenException
                : TokenException.required();

        // Reuses the global exception handler to keep the error format consistent.
        resolver.resolveException(request, response, null, exception);
    }
}
