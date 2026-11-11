package com.skylineground.modules.identity.user.cache;


import com.skylineground.modules.identity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Kept separate from {@link com.skylineground.modules.identity.user.UserReferenceProvider}
 * to ensure Spring's cache proxy intercepts the call correctly —
 * self-invocation within the same bean bypasses the proxy.
 */
@Component
@RequiredArgsConstructor
public class UserIdCacheResolver {

    private final UserRepository repository;

    @Cacheable(value = UserCacheNames.USER_INTERNAL_ID, key = "#externalId")
    public Long byExternalId(UUID externalId) {
        return repository.findIdByExternalId(externalId)
                .orElseThrow(() -> new RuntimeException("User not found with externalId: " + externalId));
    }
}
