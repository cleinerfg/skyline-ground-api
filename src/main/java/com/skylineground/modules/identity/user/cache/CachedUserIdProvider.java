package com.skylineground.modules.identity.user.cache;


import com.skylineground.modules.identity.user.persistence.UserReferenceProvider;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.shared.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Kept separate from {@link UserReferenceProvider}
 * to ensure Spring's cache proxy intercepts the call correctly —
 * self-invocation within the same bean bypasses the proxy.
 */
@Component
@RequiredArgsConstructor
public class CachedUserIdProvider {

    private final UserRepository repository;

    @Cacheable(value = UserCacheNames.USER_INTERNAL_ID, key = "#externalId")
    public Long byExternalId(UUID externalId) {
        return repository.findIdByExternalId(externalId)
                .orElseThrow(() -> AppException.systemIntegrity(
                        new RuntimeException("User is null"),
                        "Invalid DB state, User not found with externalId: " + externalId
                ));
    }
}
