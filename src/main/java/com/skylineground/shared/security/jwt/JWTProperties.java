package com.skylineground.shared.security.jwt;

import com.skylineground.shared.config.SystemConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.security.jwt")
public record JWTProperties(
        String secret,
        String issuer,
        Duration expiration
) {

    public JWTProperties {
        if (secret == null || secret.length() < SystemConstant.JWT_MIN_SECRET_LENGTH) {
            throw new IllegalArgumentException(
                    "JWT secret must contain at least %d characters."
                            .formatted(SystemConstant.JWT_MIN_SECRET_LENGTH)
            );
        }

        if (issuer == null || issuer.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT issuer must not be null or blank."
            );
        }

        if (expiration == null || expiration.compareTo(SystemConstant.JWT_MIN_TOKEN_EXPIRATION) < 0) {
            throw new IllegalArgumentException(
                    "JWT expiration must be at least %s."
                            .formatted(SystemConstant.JWT_MIN_TOKEN_EXPIRATION)
            );
        }
    }
}
