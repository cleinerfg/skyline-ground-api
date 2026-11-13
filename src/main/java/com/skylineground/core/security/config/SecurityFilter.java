package com.skylineground.core.security.config;

import com.skylineground.core.security.jwt.TokenException;
import com.skylineground.core.security.jwt.TokenPayload;
import com.skylineground.core.security.jwt.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    /**
     * Carries the token validation failure to the AuthenticationEntryPoint.
     */
    public static final String AUTH_EXCEPTION_ATTRIBUTE = "SKYLINE_SECURITY_AUTH_EXCEPTION";

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null) {
            try {
                TokenPayload tokenPayload = tokenService.validate(token);

                var authentication = new UsernamePasswordAuthenticationToken(
                        tokenPayload,
                        null,
                        Collections.emptyList()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (TokenException e) {
                // Never write the response here: the route may be public.
                // Store the failure instead; the entry point only uses it
                // if the route requires authentication.
                SecurityContextHolder.clearContext();
                request.setAttribute(AUTH_EXCEPTION_ATTRIBUTE, e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
