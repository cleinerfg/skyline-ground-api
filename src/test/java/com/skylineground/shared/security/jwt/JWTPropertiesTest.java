package com.skylineground.shared.security.jwt;

import com.skylineground.shared.config.SystemConstant;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JWTPropertiesTest {

    private static final String SECRET = "test-secret-with-enough-length-for-hmac";
    private static final String ISSUER = "skyline-ground";
    private static final Duration EXPIRATION = Duration.ofHours(1);

    @Test
    void shouldCreatePropertiesWhenAllValuesAreValid() {
        JWTProperties props = new JWTProperties(
                SECRET,
                ISSUER,
                EXPIRATION
        );

        assertThat(props.secret()).isEqualTo(SECRET);
        assertThat(props.issuer()).isEqualTo(ISSUER);
        assertThat(props.expiration()).isEqualTo(EXPIRATION);
    }

    @Test
    void shouldAcceptSecretWithMinimumLength() {
        String secret = "a".repeat(SystemConstant.JWT_MIN_SECRET_LENGTH);

        JWTProperties props = new JWTProperties(
                secret,
                ISSUER,
                EXPIRATION
        );

        assertThat(props.secret()).isEqualTo(secret);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSecretIsNull() {
        assertThatThrownBy(() -> new JWTProperties(
                null,
                ISSUER,
                EXPIRATION
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "JWT secret must contain at least %d characters."
                                .formatted(SystemConstant.JWT_MIN_SECRET_LENGTH)
                );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSecretIsTooShort() {
        String secret = "a".repeat(SystemConstant.JWT_MIN_SECRET_LENGTH - 1);

        assertThatThrownBy(() -> new JWTProperties(
                secret,
                ISSUER,
                EXPIRATION
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "JWT secret must contain at least %d characters."
                                .formatted(SystemConstant.JWT_MIN_SECRET_LENGTH)
                );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIssuerIsNull() {
        assertThatThrownBy(() -> new JWTProperties(
                SECRET,
                null,
                EXPIRATION
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("JWT issuer must not be null or blank.");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIssuerIsBlank() {
        assertThatThrownBy(() -> new JWTProperties(
                SECRET,
                "   ",
                EXPIRATION
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("JWT issuer must not be null or blank.");
    }

    @Test
    void shouldAcceptMinimumTokenExpiration() {
        JWTProperties props = new JWTProperties(
                SECRET,
                ISSUER,
                SystemConstant.JWT_MIN_TOKEN_EXPIRATION
        );

        assertThat(props.expiration())
                .isEqualTo(SystemConstant.JWT_MIN_TOKEN_EXPIRATION);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenExpirationIsNull() {
        assertThatThrownBy(() -> new JWTProperties(
                SECRET,
                ISSUER,
                null
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "JWT expiration must be at least %s."
                                .formatted(SystemConstant.JWT_MIN_TOKEN_EXPIRATION)
                );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenExpirationIsBelowMinimum() {
        Duration expiration = SystemConstant.JWT_MIN_TOKEN_EXPIRATION.minusSeconds(1);

        assertThatThrownBy(() -> new JWTProperties(
                SECRET,
                ISSUER,
                expiration
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "JWT expiration must be at least %s."
                                .formatted(SystemConstant.JWT_MIN_TOKEN_EXPIRATION)
                );
    }
}
