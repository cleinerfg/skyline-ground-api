package com.skylineground.modules.identity.user.service;

import com.skylineground.core.config.uuid.UuidGenerator;
import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;
import com.skylineground.modules.identity.user.UserMapper;
import com.skylineground.modules.identity.user.dto.UserRequest;
import com.skylineground.modules.identity.user.dto.UserResponse;
import com.skylineground.modules.identity.user.exception.InvalidPasswordException;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    private static final String VALID_PASSWORD = "SecurePassword125!#";

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UuidGenerator uuidGenerator;

    @InjectMocks
    private CreateUserService createUserService;

    @Test
    void shouldCreateUserSuccessfully() {
        UUID generatedExternalId = UUID.randomUUID();
        String rawPassword = VALID_PASSWORD;
        String encodedPassword = "encodedPassword";

        UserRequest request = createUserRequest();

        User user = new User();
        user.setPassword(rawPassword);

        User savedUser = new User();
        UserResponse expectedResponse = mock(UserResponse.class);

        when(repository.existsByEmail(request.email())).thenReturn(false);
        when(uuidGenerator.generate()).thenReturn(generatedExternalId);
        when(mapper.toEntity(generatedExternalId, request)).thenReturn(user);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(repository.save(user)).thenReturn(savedUser);
        when(mapper.toResponse(savedUser)).thenReturn(expectedResponse);

        UserResponse response = createUserService.execute(request);

        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);

        assertThat(user.getPassword()).isEqualTo(encodedPassword);

        verify(repository).existsByEmail(request.email());
        verify(uuidGenerator).generate();
        verify(mapper).toEntity(generatedExternalId, request);
        verify(passwordEncoder).encode(rawPassword);
        verify(repository).save(user);
        verify(mapper).toResponse(savedUser);
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenPasswordIsInvalid() {
        UserRequest request = UserRequest.builder()
                .name("Jeremy Gilbert")
                .email("jeremy@example.com")
                .password("invalid")
                .build();

        assertThatThrownBy(() -> createUserService.execute(request))
                .isInstanceOf(InvalidPasswordException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_PASSWORD);

        verifyNoInteractions(
                repository,
                mapper,
                passwordEncoder,
                uuidGenerator
        );
    }

    @Test
    void shouldThrowEmailAlreadyExistsExceptionWhenEmailExists() {
        UserRequest request = createUserRequest();

        when(repository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> createUserService.execute(request))
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);

        verify(repository).existsByEmail(request.email());

        verifyNoMoreInteractions(repository);
        verifyNoInteractions(
                uuidGenerator,
                mapper,
                passwordEncoder
        );
    }

    private UserRequest createUserRequest() {
        return UserRequest.builder()
                .name("Jeremy Gilbert")
                .email("jeremy@example.com")
                .password(VALID_PASSWORD)
                .build();
    }
}
