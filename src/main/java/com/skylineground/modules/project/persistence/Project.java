package com.skylineground.modules.project.persistence;

import com.skylineground.core.common.BaseEntity;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.project.constant.ProjectStatus;
import com.skylineground.modules.project.constant.ProjectType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "project")
@Getter @Setter
@NoArgsConstructor
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "external_id", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID externalId;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", nullable = false, columnDefinition = "project_type")
    private ProjectType type;

    @NotNull
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "project_status")
    private ProjectStatus status = ProjectStatus.PLANNING;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "expected_end_date")
    private LocalDate expectedEndDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @Setter(AccessLevel.NONE)
    private User createdBy;

    @Builder
    public Project(
            UUID externalId,
            String name,
            String description,
            ProjectType type,
            LocalDate startDate,
            LocalDate expectedEndDate,
            User createdBy
    ) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.createdBy = createdBy;
    }
}
