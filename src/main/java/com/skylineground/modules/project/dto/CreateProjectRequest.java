package com.skylineground.modules.project.dto;

import com.skylineground.modules.project.constant.ProjectType;
import com.skylineground.shared.exception.ValidationMessage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateProjectRequest(

        @NotNull(message = ValidationMessage.REQUIRED)
        @Size(max = 255, message = ValidationMessage.MAX_LENGTH_255)
        String name,

        String description,

        @NotNull(message = ValidationMessage.REQUIRED)
        ProjectType type,

        @NotNull(message = ValidationMessage.REQUIRED)
        LocalDate startDate,

        LocalDate expectedEndDate
) {
}
