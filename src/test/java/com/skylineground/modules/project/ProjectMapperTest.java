package com.skylineground.modules.project;

import com.skylineground.modules.identity.user.dto.UserSummary;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.project.constant.ProjectStatus;
import com.skylineground.modules.project.constant.ProjectType;
import com.skylineground.modules.project.dto.ProjectRequest;
import com.skylineground.modules.project.dto.ProjectResponse;
import com.skylineground.modules.project.persistence.Project;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectMapperTest {

    private static final ProjectStatus DEFAULT_STATUS = ProjectStatus.PLANNING;

    private final ProjectMapper mapper = new ProjectMapper();

    @Test
    void shouldMapRequestToEntity() {
        User creator = mock(User.class);
        ProjectType type = ProjectType.BUILD;

        LocalDate startDate = LocalDate.of(2026, 1, 10);
        LocalDate expectedEndDate = LocalDate.of(2026, 6, 10);

        ProjectRequest request = ProjectRequest.builder()
                .name("Project Alpha")
                .description("Project description")
                .type(type)
                .startDate(startDate)
                .expectedEndDate(expectedEndDate)
                .build();

        Project result = mapper.toEntity(request, creator);

        assertThat(result.getName()).isEqualTo(request.name());
        assertThat(result.getDescription()).isEqualTo(request.description());
        assertThat(result.getType()).isEqualTo(request.type());
        assertThat(result.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(result.getStartDate()).isEqualTo(request.startDate());
        assertThat(result.getExpectedEndDate()).isEqualTo(request.expectedEndDate());
        assertThat(result.getCreatedBy()).isSameAs(creator);
    }

    @Test
    void shouldMapEntityToResponse() {
        Project entity = mock(Project.class);
        UserSummary createdBySummary = mock(UserSummary.class);

        UUID externalId = UUID.randomUUID();
        ProjectType type = ProjectType.PURCHASE;
        ProjectStatus status = ProjectStatus.COMPLETED;

        LocalDate startDate = LocalDate.of(2026, 1, 10);
        LocalDate expectedEndDate = LocalDate.of(2026, 6, 10);
        LocalDate endDate = LocalDate.of(2026, 5, 20);

        when(entity.getExternalId()).thenReturn(externalId);
        when(entity.getName()).thenReturn("Project Alpha");
        when(entity.getDescription()).thenReturn("Project description");
        when(entity.getType()).thenReturn(type);
        when(entity.getStartDate()).thenReturn(startDate);
        when(entity.getExpectedEndDate()).thenReturn(expectedEndDate);
        when(entity.getEndDate()).thenReturn(endDate);
        when(entity.getStatus()).thenReturn(status);

        ProjectResponse result = mapper.toResponse(entity, createdBySummary);

        assertThat(result.externalId()).isEqualTo(externalId);
        assertThat(result.name()).isEqualTo("Project Alpha");
        assertThat(result.description()).isEqualTo("Project description");
        assertThat(result.type()).isEqualTo(type);
        assertThat(result.status()).isEqualTo(status);
        assertThat(result.startDate()).isEqualTo(startDate);
        assertThat(result.expectedEndDate()).isEqualTo(expectedEndDate);
        assertThat(result.endDate()).isEqualTo(endDate);
        assertThat(result.createdBy()).isSameAs(createdBySummary);
    }
}
