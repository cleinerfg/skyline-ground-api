package com.skylineground.shared.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.skylineground.modules.identity.user.constant.SystemRole;
import com.skylineground.modules.identity.user.persistence.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TokenService {

    private final JWTProperties props;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public TokenService(JWTProperties props) {
        this.props = props;
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
                .withExpiresAt(now.plus(props.expiration()))
                .sign(algorithm);
    }

    public TokenPayload validate(String token) {
        if (token == null || token.isBlank()) {
            throw TokenException.invalid();
        }

        try {
            DecodedJWT jwt = verifier.verify(token);

            return TokenPayload.builder()
                    .externalId(UUID.fromString(jwt.getSubject()))
                    .systemRole(
                            SystemRole.valueOf(
                                    jwt.getClaim("systemRole").asString()
                            )
                    )
                    .build();

        } catch (TokenExpiredException e) {
            throw TokenException.expired();

        } catch (JWTVerificationException | IllegalArgumentException e) {
            throw TokenException.invalid();
        }
    }
}
