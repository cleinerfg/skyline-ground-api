package com.skylineground.shared.security;

import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.security.jwt.TokenException;
import com.skylineground.shared.security.jwt.TokenPayload;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

    public TokenPayload getAuthenticatedPayload() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw TokenException.required();
        }

        try {
            return (TokenPayload) authentication.getPrincipal();
        } catch (ClassCastException e) {
            throw AppException.systemIntegrity(e, "SecurityContextService must be" +
                    " used in a authenticated context, check authenticated routes in SecurityConfig."
            );
        }
    }
}
