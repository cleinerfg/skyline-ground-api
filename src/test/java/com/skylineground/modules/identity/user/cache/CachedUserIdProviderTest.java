package com.skylineground.modules.identity.user.cache;

import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.shared.exception.AppException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CachedUserIdProviderTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private CachedUserIdProvider provider;

    @Test
    void shouldReturnInternalIdWhenUserExists() {
        UUID externalId = UUID.randomUUID();
        Long expectedId = 1L;

        when(repository.findIdByExternalId(externalId))
                .thenReturn(Optional.of(expectedId));

        Long result = provider.byExternalId(externalId);

        assertThat(result).isEqualTo(expectedId);

        verify(repository).findIdByExternalId(externalId);
    }

    @Test
    void shouldThrowSystemIntegrityExceptionWhenUserDoesNotExist() {
        UUID externalId = UUID.randomUUID();

        when(repository.findIdByExternalId(externalId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> provider.byExternalId(externalId))
                .isInstanceOf(AppException.class);

        verify(repository).findIdByExternalId(externalId);
    }
}
