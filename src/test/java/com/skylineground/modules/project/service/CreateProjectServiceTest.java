package com.skylineground.modules.project.service;

import com.skylineground.core.config.uuid.UuidGenerator;
import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;
import com.skylineground.modules.identity.user.dto.UserSummary;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserReferenceProvider;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.modules.project.ProjectMapper;
import com.skylineground.modules.project.constant.ProjectType;
import com.skylineground.modules.project.dto.ProjectRequest;
import com.skylineground.modules.project.dto.ProjectResponse;
import com.skylineground.modules.project.exception.ProjectAlreadyExistsException;
import com.skylineground.modules.project.persistence.Project;
import com.skylineground.modules.project.persistence.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserReferenceProvider userReferenceProvider;

    @Mock
    private UuidGenerator uuidGenerator;

    @Mock
    private ProjectMapper mapper;

    @InjectMocks
    private CreateProjectService createProjectService;

    @Test
    void shouldCreateProjectSuccessfully() {
        UUID creatorExternalId = UUID.randomUUID();
        UUID generatedExternalId = UUID.randomUUID();
        LocalDate startDate = LocalDate.now();
        LocalDate expectedEndDate = LocalDate.now().plusDays(10);

        ProjectRequest request = createProjectRequest(startDate, expectedEndDate);

        User creatorMock = mock(User.class);
        Project unmappedProject = new Project();
        Project savedProject = new Project();
        UserSummary creatorSummaryMock = mock(UserSummary.class);
        ProjectResponse expectedResponse = mock(ProjectResponse.class);

        when(projectRepository.findDuplicate(
                creatorExternalId,
                request.name(),
                request.type(),
                request.startDate())
        ).thenReturn(Optional.empty());

        when(userReferenceProvider.provide(creatorExternalId)).thenReturn(creatorMock);
        when(mapper.toEntity(request, creatorMock)).thenReturn(unmappedProject);
        when(uuidGenerator.generate()).thenReturn(generatedExternalId);
        when(projectRepository.save(unmappedProject)).thenReturn(savedProject);
        when(userRepository.findSummaryByExternalId(creatorExternalId)).thenReturn(creatorSummaryMock);
        when(mapper.toResponse(savedProject, creatorSummaryMock)).thenReturn(expectedResponse);

        ProjectResponse response = createProjectService.execute(creatorExternalId, request);

        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
        assertThat(unmappedProject.getExternalId())
                .isEqualTo(generatedExternalId);

        verify(projectRepository)
                .findDuplicate(
                        creatorExternalId,
                        request.name(),
                        request.type(),
                        request.startDate()
                );
        verify(userReferenceProvider).provide(creatorExternalId);
        verify(projectRepository).save(unmappedProject);
        verify(userRepository).findSummaryByExternalId(creatorExternalId);
    }


    @Test
    void shouldThrowAppExceptionWhenDatesAreInvalid() {
        UUID creatorExternalId = UUID.randomUUID();
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate expectedEndDate = LocalDate.now();

        ProjectRequest request = createProjectRequest(startDate, expectedEndDate);

        assertThatThrownBy(() -> createProjectService.execute(creatorExternalId, request))
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_PERIOD);

        verifyNoInteractions(
                projectRepository,
                userReferenceProvider,
                userRepository,
                uuidGenerator,
                mapper
        );
    }

    @Test
    void shouldThrowProjectAlreadyExistsExceptionWhenProjectExists() {
        UUID creatorExternalId = UUID.randomUUID();
        UUID existingProjectId = UUID.randomUUID();
        LocalDate startDate = LocalDate.now();
        LocalDate expectedEndDate = LocalDate.now().plusDays(10);

        ProjectRequest request = createProjectRequest(startDate, expectedEndDate);

        when(projectRepository.findDuplicate(
                creatorExternalId,
                request.name(),
                request.type(),
                request.startDate())
        ).thenReturn(Optional.of(existingProjectId));

        assertThatThrownBy(() -> createProjectService.execute(creatorExternalId, request))
                .isInstanceOf(ProjectAlreadyExistsException.class)
                .extracting("existingProjectId")
                .isEqualTo(existingProjectId);

        verify(projectRepository).findDuplicate(
                creatorExternalId,
                request.name(),
                request.type(),
                request.startDate()
        );
        verifyNoMoreInteractions(projectRepository);
        verifyNoInteractions(
                userReferenceProvider,
                userRepository,
                uuidGenerator,
                mapper
        );
    }

    private ProjectRequest createProjectRequest(LocalDate startDate, LocalDate expectedEndDate) {
        return ProjectRequest.builder()
                .name("Skyline Tower")
                .type(ProjectType.BUILD)
                .startDate(startDate)
                .expectedEndDate(expectedEndDate)
                .build();
    }
}
