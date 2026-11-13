package com.skylineground.modules.project.dto;

import com.skylineground.modules.project.constant.ProjectType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProjectRequest(
        String name,
        String description,
        ProjectType type,
        LocalDate startDate,
        LocalDate expectedEndDate
) {
}
