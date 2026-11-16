package com.skylineground.modules.project.service;

import com.skylineground.core.config.uuid.UuidGenerator;
import com.skylineground.core.exception.AppException;
import com.skylineground.modules.identity.user.dto.UserSummary;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserReferenceProvider;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.modules.project.ProjectMapper;
import com.skylineground.modules.project.dto.ProjectRequest;
import com.skylineground.modules.project.dto.ProjectResponse;
import com.skylineground.modules.project.exception.ProjectAlreadyExistsException;
import com.skylineground.modules.project.persistence.Project;
import com.skylineground.modules.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserReferenceProvider userReferenceProvider;
    private final UuidGenerator uuidGenerator;
    private final ProjectMapper mapper;

    @Transactional
    public ProjectResponse execute(UUID creatorExternalId, ProjectRequest request) {
        validateDates(
                request.startDate(),
                request.expectedEndDate()
        );

        projectRepository.findDuplicate(
                creatorExternalId,
                request.name(),
                request.type(),
                request.startDate()
        ).ifPresent(existingId -> {
            throw new ProjectAlreadyExistsException(existingId);
        });


        User creator = userReferenceProvider.provide(creatorExternalId);

        Project project = mapper.toEntity(request, creator);
        project.setExternalId(uuidGenerator.generate());

        Project savedProject = projectRepository.save(project);

        UserSummary creatorSummary = userRepository
                .findSummaryByExternalId(creatorExternalId);

        return mapper.toResponse(
                savedProject,
                creatorSummary
        );
    }

    private void validateDates(LocalDate startDate, LocalDate expectedEndDate) {
        if (startDate.isAfter(expectedEndDate)) {
            throw AppException.invalidPeriod(
                    startDate.toString(),
                    expectedEndDate.toString()
            );
        }
    }
}
