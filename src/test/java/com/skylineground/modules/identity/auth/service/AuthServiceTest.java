package com.skylineground.modules.identity.auth.service;

import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldReturnUserDetailsWhenUserExists() {
        User user = User.builder()
                .email("jeremy@example.com")
                .build();

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        UserDetails result = authService.loadUserByUsername(user.getEmail());

        assertThat(result).isSameAs(user);
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        String email = "other@example.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with username: " + email);
    }
}
