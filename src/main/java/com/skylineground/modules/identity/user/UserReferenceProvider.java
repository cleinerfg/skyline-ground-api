package com.skylineground.modules.identity.user;

import com.skylineground.modules.identity.user.cache.UserIdCacheResolver;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserReferenceProvider {

    private final UserIdCacheResolver userIdCacheResolver;
    private final EntityManager entityManager;

    public User provide(UUID externalId) {
        Long id = userIdCacheResolver.byExternalId(externalId);
        return entityManager.getReference(User.class, id);
    }
}
