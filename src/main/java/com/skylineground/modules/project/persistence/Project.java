package com.skylineground.modules.project.persistence;

import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.project.constant.ProjectStatus;
import com.skylineground.modules.project.constant.ProjectType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "project")
@Getter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "external_id", nullable = false, unique = true, updatable = false)
    private UUID externalId;

    @NotNull
    @Size(max = 255)
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "description")
    private String description;

    @NotNull
    @Setter
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", nullable = false, columnDefinition = "project_type")
    private ProjectType type;

    @NotNull
    @Setter
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "project_status")
    private ProjectStatus status = ProjectStatus.PLANNING;

    @NotNull
    @Setter
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Setter
    @Column(name = "expected_end_date")
    private LocalDate expectedEndDate;

    @Setter
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Builder
    public Project(
            UUID externalId,
            String name,
            String description,
            ProjectType type,
            LocalDate startDate,
            LocalDate expectedEndDate,
            User creator
    ) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.creator = creator;
    }
}
