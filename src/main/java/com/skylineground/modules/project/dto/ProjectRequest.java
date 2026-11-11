package com.skylineground.modules.project.dto;

import com.skylineground.modules.project.constant.ProjectStatus;
import com.skylineground.modules.project.constant.ProjectType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProjectRequest(
        String description,
        String name,
        ProjectType type,
        LocalDate startDate,
        LocalDate expectedEndDate,
        ProjectStatus status
) {
}
