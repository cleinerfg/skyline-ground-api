package com.skylineground.core.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.skylineground.modules.identity.user.constant.SystemRole;
import com.skylineground.modules.identity.user.persistence.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private final JWTProperties props;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public TokenService(JWTProperties props) {
        this.props = Objects.requireNonNull(props, "JWT Properties must not be null");

        Objects.requireNonNull(props.secret(), "JWT Secret must not be null");
        Objects.requireNonNull(props.issuer(), "JWT Issuer must not be null");

        this.algorithm = Algorithm.HMAC256(props.secret());
        this.verifier = JWT.require(algorithm)
                .withIssuer(props.issuer())
                .acceptLeeway(5)
                .build();
    }

    public String generate(User user) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(props.issuer())
                .withSubject(user.getExternalId().toString())
                .withClaim("systemRole", user.getSystemRole().name())
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(props.expirationSeconds()))
                .sign(algorithm);
    }

    public Optional<TokenPayload> validate(String token) {
        if (token == null || token.isBlank()) return Optional.empty();

        try {
            DecodedJWT jwt = verifier.verify(token);

            var externalId = UUID.fromString(jwt.getSubject());
            var systemRole = SystemRole.valueOf(jwt.getClaim("systemRole").asString());

            return Optional.of(TokenPayload.builder()
                    .externalId(externalId)
                    .systemRole(systemRole)
                    .build());

        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }
}
