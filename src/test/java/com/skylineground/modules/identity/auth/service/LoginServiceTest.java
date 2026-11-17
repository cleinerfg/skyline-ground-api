package com.skylineground.modules.identity.auth.service;

import com.skylineground.modules.identity.auth.dto.LoginRequest;
import com.skylineground.modules.identity.auth.dto.LoginResponse;
import com.skylineground.modules.identity.auth.exception.InvalidCredentialsException;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.shared.security.jwt.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
    void shouldAuthenticateSuccessfully() {
        String email = "jeremy@example.com";
        String password = "SecurePassword35!";
        String encodedPassword = "encoded-password";
        String expectedToken = "jwt-token";

        LoginRequest request = new LoginRequest(email, password);

        User user = mock(User.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(encodedPassword);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(tokenService.generate(user)).thenReturn(expectedToken);

        LoginResponse response = loginService.authenticate(request);

        assertThat(response)
                .isNotNull()
                .extracting(LoginResponse::token)
                .isEqualTo(expectedToken);

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, encodedPassword);
        verify(tokenService).generate(user);
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenUserDoesNotExist() {
        String email = "jeremy@example.com";

        LoginRequest request = new LoginRequest(
                email,
                "SecurePassword35!"
        );

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.authenticate(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(passwordEncoder, tokenService);
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenPasswordIsIncorrect() {
        String email = "jeremy@example.com";
        String password = "InvalidPassword123!";
        String encodedPassword = "encoded-password";

        LoginRequest request = new LoginRequest(email, password);

        User user = mock(User.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(encodedPassword);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        assertThatThrownBy(() -> loginService.authenticate(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, encodedPassword);
        verifyNoInteractions(tokenService);
    }
}
