package com.skylineground.modules.identity.user.persistence;

import com.skylineground.modules.identity.user.cache.CachedUserIdProvider;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserReferenceProvider {

    private final CachedUserIdProvider cachedUserIdProvider;
    private final EntityManager entityManager;

    public User provide(UUID externalId) {
        Long id = cachedUserIdProvider.byExternalId(externalId);
        return entityManager.getReference(User.class, id);
    }
}
