package com.skylineground.modules.project.dto;

import com.skylineground.modules.identity.user.dto.UserSummary;
import com.skylineground.modules.project.constant.ProjectStatus;
import com.skylineground.modules.project.constant.ProjectType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record ProjectResponse(
        UUID externalId,
        String description,
        String name,
        ProjectType type,
        LocalDate startDate,
        LocalDate expectedEndDate,
        LocalDate endDate,
        ProjectStatus status,
        UserSummary createdBy
) {
}
