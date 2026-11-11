package com.skylineground.modules.project.service;

import com.skylineground.modules.identity.user.User;
import com.skylineground.modules.identity.user.UserReferenceProvider;
import com.skylineground.modules.identity.user.UserRepository;
import com.skylineground.modules.identity.user.dto.UserSummary;
import com.skylineground.modules.project.Project;
import com.skylineground.modules.project.ProjectMapper;
import com.skylineground.modules.project.ProjectRepository;
import com.skylineground.modules.project.dto.ProjectRequest;
import com.skylineground.modules.project.dto.ProjectResponse;
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
