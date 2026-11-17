package com.skylineground.modules.identity.auth.service;

import com.skylineground.modules.identity.auth.dto.LoginRequest;
import com.skylineground.modules.identity.auth.dto.LoginResponse;
import com.skylineground.modules.identity.auth.exception.InvalidCredentialsException;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.shared.security.jwt.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginService loginService;

    @Test
    void shouldAuthenticateSuccessfully() {
        String email = "jeremy@example.com";
        String password = "SecurePassword123!";
        String expectedToken = "jwt-token";

        LoginRequest request = new LoginRequest(email, password);

        User user = mock(User.class);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generate(user)).thenReturn(expectedToken);

        LoginResponse response = loginService.authenticate(request);

        assertThat(response)
                .isNotNull()
                .extracting(LoginResponse::token)
                .isEqualTo(expectedToken);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication).getPrincipal();
        verify(tokenService).generate(user);
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenCredentialsAreInvalid() {
        LoginRequest request = new LoginRequest(
                "jeremy@example.com",
                "InvalidPassword123!"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThatThrownBy(() -> loginService.authenticate(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(tokenService);
    }
}
