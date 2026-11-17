package com.skylineground.modules.identity.user;

import com.skylineground.modules.identity.user.constant.SystemRole;
import com.skylineground.modules.identity.user.dto.CreateUserRequest;
import com.skylineground.modules.identity.user.dto.UserResponse;
import com.skylineground.modules.identity.user.persistence.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void shouldMapRequestToEntity() {
        UUID externalId = UUID.randomUUID();

        CreateUserRequest request = CreateUserRequest.builder()
                .name("Jeremy Gilbert")
                .email("jeremy@example.com")
                .password("SecurePassword123!")
                .build();

        User result = mapper.toEntity(externalId, request);

        assertThat(result.getExternalId()).isEqualTo(externalId);
        assertThat(result.getName()).isEqualTo(request.name());
        assertThat(result.getEmail()).isEqualTo(request.email());
        assertThat(result.getPassword()).isEqualTo(request.password());
    }

    @Test
    void shouldMapEntityToResponse() {
        User entity = mock(User.class);

        UUID externalId = UUID.randomUUID();
        SystemRole systemRole = SystemRole.MEMBER;

        when(entity.getExternalId()).thenReturn(externalId);
        when(entity.getName()).thenReturn("Jeremy Gilbert");
        when(entity.getEmail()).thenReturn("jeremy@example.com");
        when(entity.getSystemRole()).thenReturn(systemRole);

        UserResponse result = mapper.toResponse(entity);

        assertThat(result.externalId()).isEqualTo(externalId);
        assertThat(result.name()).isEqualTo("Jeremy Gilbert");
        assertThat(result.email()).isEqualTo("jeremy@example.com");
        assertThat(result.systemRole()).isEqualTo(systemRole);
    }
}
