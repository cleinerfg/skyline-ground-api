package com.skylineground.core.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.jwt")
public record JWTProperties(
        String secret,
        String issuer,
        long expirationSeconds
) {
}
