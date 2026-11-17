package com.skylineground.shared.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.skylineground.modules.identity.user.constant.SystemRole;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.shared.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenServiceTest {

    private static final String SECRET = "test-secret-with-enough-length-for-hmac";
    private static final String ISSUER = "skyline-ground";
    private static final Duration EXPIRATION = Duration.ofHours(1);

    private TokenService service;
    private User user;

    private final UUID externalId = UUID.randomUUID();
    private final SystemRole systemRole = SystemRole.MEMBER;

    @BeforeEach
    void setUp() {
        JWTProperties props = new JWTProperties(
                SECRET,
                ISSUER,
                EXPIRATION
        );

        service = new TokenService(props);

        user = mock(User.class);

        when(user.getExternalId()).thenReturn(externalId);
        when(user.getSystemRole()).thenReturn(systemRole);
    }

    @Test
    void shouldGenerateValidToken() {
        String token = service.generate(user);

        assertThat(token).isNotBlank();

        var decoded = JWT.decode(token);

        assertThat(decoded.getIssuer()).isEqualTo(ISSUER);
        assertThat(decoded.getSubject())
                .isEqualTo(user.getExternalId().toString());
        assertThat(decoded.getClaim("systemRole").asString())
                .isEqualTo(user.getSystemRole().name());

        assertThat(decoded.getIssuedAt()).isNotNull();
        assertThat(decoded.getExpiresAt()).isNotNull();

        assertThat(decoded.getExpiresAt().toInstant())
                .isEqualTo(
                        decoded.getIssuedAt().toInstant()
                                .plus(EXPIRATION)
                );
    }

    @Test
    void shouldValidateValidToken() {
        String token = service.generate(user);

        TokenPayload result = service.validate(token);

        assertThat(result)
                .isNotNull()
                .satisfies(payload -> {
                    assertThat(payload.externalId()).isEqualTo(externalId);
                    assertThat(payload.systemRole()).isEqualTo(systemRole);
                });
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenTokenIsNull() {
        assertTokenException(
                () -> service.validate(null),
                ErrorCode.TOKEN_INVALID
        );
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenTokenIsBlank() {
        assertTokenException(
                () -> service.validate(""),
                ErrorCode.TOKEN_INVALID
        );

        assertTokenException(
                () -> service.validate(" "),
                ErrorCode.TOKEN_INVALID
        );
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenTokenIsMalformed() {
        assertTokenException(
                () -> service.validate("invalid.token.value"),
                ErrorCode.TOKEN_INVALID
        );
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenSignatureIsInvalid() {
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withSubject(externalId.toString())
                .withClaim("systemRole", systemRole.name())
                .sign(Algorithm.HMAC256("another-secret"));

        assertTokenException(
                () -> service.validate(token),
                ErrorCode.TOKEN_INVALID
        );
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenIssuerIsInvalid() {
        String token = JWT.create()
                .withIssuer("another-issuer")
                .withSubject(externalId.toString())
                .withClaim("systemRole", systemRole.name())
                .sign(Algorithm.HMAC256(SECRET));

        assertTokenException(
                () -> service.validate(token),
                ErrorCode.TOKEN_INVALID
        );
    }

    @Test
    void shouldThrowExpiredTokenExceptionWhenTokenIsExpired() {
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withSubject(externalId.toString())
                .withClaim("systemRole", systemRole.name())
                .withExpiresAt(Instant.now().minusSeconds(60))
                .sign(Algorithm.HMAC256(SECRET));

        assertTokenException(
                () -> service.validate(token),
                ErrorCode.TOKEN_EXPIRED
        );
    }

    private void assertTokenException(
            Runnable action,
            ErrorCode expectedErrorCode
    ) {
        assertThatThrownBy(action::run)
                .isInstanceOfSatisfying(TokenException.class, ex ->
                        assertThat(ex.getErrorCode())
                                .isEqualTo(expectedErrorCode)
                );
    }
}
