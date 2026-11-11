package com.skylineground.modules.project.service;

import com.skylineground.modules.identity.user.dto.UserSummary;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserReferenceProvider;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.modules.project.ProjectMapper;
import com.skylineground.modules.project.dto.ProjectRequest;
import com.skylineground.modules.project.dto.ProjectResponse;
import com.skylineground.modules.project.persistence.Project;
import com.skylineground.modules.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserReferenceProvider userReferenceProvider;
    private final ProjectMapper mapper;

    public ProjectResponse execute(UUID creatorExternalId, ProjectRequest request) {
        validateDates(
                request.startDate(),
                request.expectedEndDate()
        );
        if (projectRepository.existsByName(request.name())) {
            throw new RuntimeException("Project already exists");
        }

        User creator = userReferenceProvider.provide(creatorExternalId);
        Project project = mapper.toEntity(request, creator);

        UserSummary createdBySummary = userRepository
                .findSummaryByExternalId(creatorExternalId);

        return mapper.toResponse(
                projectRepository.save(project),
                createdBySummary
        );
    }

    private void validateDates(LocalDate startDate, LocalDate expectedEndDate) {
        if (startDate != null && expectedEndDate != null && startDate.isAfter(expectedEndDate)) {
            throw new IllegalArgumentException("The start date cannot be after the expected end date.");
        }
    }
}
