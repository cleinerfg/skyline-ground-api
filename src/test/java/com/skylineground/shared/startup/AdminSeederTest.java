package com.skylineground.shared.startup;

import com.skylineground.modules.identity.user.constant.SystemRole;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.shared.config.uuid.UuidGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminSeederTest {

    @Mock
    private AdminProperties props;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UuidGenerator uuidGenerator;

    @InjectMocks
    private AdminSeeder adminSeeder;

    @Test
    void shouldCreateAndSaveAdminWhenAdminDoesNotExist() {
        var externalId = UUID.randomUUID();
        var password = "V7!mQ2#rT9";

        when(userRepository.existsBySystemRole(SystemRole.ADMIN))
                .thenReturn(false);
        when(uuidGenerator.generate()).thenReturn(externalId);
        when(props.name()).thenReturn("Admin");
        when(props.email()).thenReturn("admin@example.com");
        when(props.password()).thenReturn(password);
        when(passwordEncoder.encode(password))
                .thenReturn("encoded-password");

        adminSeeder.run();

        var captor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        var admin = captor.getValue();

        assertThat(admin.getExternalId()).isEqualTo(externalId);
        assertThat(admin.getName()).isEqualTo("Admin");
        assertThat(admin.getEmail()).isEqualTo("admin@example.com");
        assertThat(admin.getPassword()).isEqualTo("encoded-password");
        assertThat(admin.getSystemRole()).isEqualTo(SystemRole.ADMIN);

        verify(uuidGenerator).generate();
        verify(passwordEncoder).encode(password);
    }

    @Test
    void shouldNotCreateAdminWhenAdminAlreadyExists() {
        when(userRepository.existsBySystemRole(SystemRole.ADMIN))
                .thenReturn(true);

        adminSeeder.run();

        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(uuidGenerator, passwordEncoder, props);
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenAdminNameIsMissing() {
        when(userRepository.existsBySystemRole(SystemRole.ADMIN))
                .thenReturn(false);
        when(props.name()).thenReturn(null);

        assertThatThrownBy(() -> adminSeeder.run())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot initialize application: admin name is not configured.");

        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(uuidGenerator, passwordEncoder);
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenAdminEmailIsMissing() {
        when(userRepository.existsBySystemRole(SystemRole.ADMIN))
                .thenReturn(false);
        when(props.name()).thenReturn("Admin");
        when(props.email()).thenReturn(null);

        assertThatThrownBy(() -> adminSeeder.run())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot initialize application: admin email is not configured.");

        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(uuidGenerator, passwordEncoder);
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenAdminPasswordIsInvalid() {
        when(userRepository.existsBySystemRole(SystemRole.ADMIN))
                .thenReturn(false);
        when(props.name()).thenReturn("Admin");
        when(props.email()).thenReturn("admin@example.com");
        when(props.password()).thenReturn("weak");

        assertThatThrownBy(() -> adminSeeder.run())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot initialize application: configured admin password is invalid.")
                .hasCauseInstanceOf(RuntimeException.class);

        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(uuidGenerator, passwordEncoder);
    }
}
