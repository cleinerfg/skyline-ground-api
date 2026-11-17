package com.skylineground.shared.security;

import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.exception.ErrorCode;
import com.skylineground.shared.security.jwt.TokenException;
import com.skylineground.shared.security.jwt.TokenPayload;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class SecurityContextServiceTest {

    private final SecurityContextService securityContextService = new SecurityContextService();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnAuthenticatedPayload() {
        TokenPayload payload = mock(TokenPayload.class);

        var authentication = new UsernamePasswordAuthenticationToken(
                payload,
                null,
                List.of()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenPayload result = securityContextService.getAuthenticatedPayload();

        assertThat(result).isSameAs(payload);
    }

    @Test
    void shouldThrowTokenExceptionWhenAuthenticationIsMissing() {
        assertThatThrownBy(securityContextService::getAuthenticatedPayload)
                .isInstanceOf(TokenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.TOKEN_REQUIRED);
    }

    @Test
    void shouldThrowTokenExceptionWhenAuthenticationIsNotAuthenticated() {
        var authentication = UsernamePasswordAuthenticationToken.unauthenticated(
                "user",
                "password"
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThatThrownBy(securityContextService::getAuthenticatedPayload)
                .isInstanceOf(TokenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.TOKEN_REQUIRED);
    }

    @Test
    void shouldThrowAppExceptionWhenPrincipalHasInvalidType() {
        var authentication = new UsernamePasswordAuthenticationToken(
                "invalid-principal",
                null,
                List.of()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThatThrownBy(securityContextService::getAuthenticatedPayload)
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SYSTEM_INTEGRITY_ERROR);
    }
}
