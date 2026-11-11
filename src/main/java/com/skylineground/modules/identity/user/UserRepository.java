package com.skylineground.modules.identity.user;

import com.skylineground.modules.identity.user.constant.SystemRole;
import com.skylineground.modules.identity.user.dto.UserSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.externalId = :externalId")
    Optional<Long> findIdByExternalId(@Param("externalId") UUID externalId);

    boolean existsByEmail(String email);

    boolean existsBySystemRole(SystemRole role);

    @Query("""
            SELECT u.externalId AS externalId, u.name AS name, u.email AS email
            FROM User u
            WHERE u.externalId = :externalId
            """)
    UserSummary findSummaryByExternalId(@Param("externalId") UUID externalId);
}
