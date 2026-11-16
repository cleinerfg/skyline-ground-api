package com.skylineground.modules.project.persistence;

import com.skylineground.modules.project.constant.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
            SELECT p.externalId FROM Project p
            WHERE p.creator.externalId = :creatorExternalId
              AND lower(p.name) = lower(:name)
              AND p.type = :type
              AND p.startDate = :startDate
            """)
    Optional<UUID> findDuplicate(
            UUID creatorExternalId,
            String name,
            ProjectType type,
            LocalDate startDate
    );
}
