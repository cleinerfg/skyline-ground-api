package com.skylineground.core.security;

import com.skylineground.core.exception.AuthenticationRequiredException;
import com.skylineground.core.security.jwt.TokenPayload;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

    public TokenPayload getAuthenticatedPayload() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationRequiredException();
        }

        return (TokenPayload) authentication.getPrincipal();
    }
}
