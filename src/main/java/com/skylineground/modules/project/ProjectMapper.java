package com.skylineground.modules.project;

import com.skylineground.modules.identity.user.User;
import com.skylineground.modules.identity.user.dto.UserSummary;
import com.skylineground.modules.project.dto.ProjectRequest;
import com.skylineground.modules.project.dto.ProjectResponse;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectRequest request, User creator) {
        return Project.builder()
                .name(request.name())
                .description(request.description())
                .type(request.type())
                .startDate(request.startDate())
                .expectedEndDate(request.expectedEndDate())
                .status(request.status())
                .createdBy(creator)
                .build();
    }

    public ProjectResponse toResponse(Project entity, UserSummary createdBySummary) {
        return ProjectResponse.builder()
                .externalId(entity.getExternalId())
                .name(entity.getName())
                .description(entity.getDescription())
                .type(entity.getType())
                .startDate(entity.getStartDate())
                .expectedEndDate(entity.getExpectedEndDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .createdBy(createdBySummary)
                .build();
    }
}
