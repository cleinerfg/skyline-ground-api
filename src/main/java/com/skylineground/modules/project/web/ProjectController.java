package com.skylineground.modules.project.web;

import com.skylineground.core.security.SecurityContextService;
import com.skylineground.core.security.jwt.TokenPayload;
import com.skylineground.modules.project.dto.ProjectRequest;
import com.skylineground.modules.project.dto.ProjectResponse;
import com.skylineground.modules.project.service.CreateProjectService;
import com.skylineground.modules.project.web.doc.CreateProjectDoc;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Projects", description = "Endpoints responsible for project management and creation")
@RestController
@RequestMapping(value = "/api/v1/projects", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProjectController {

    private final CreateProjectService createService;
    private final SecurityContextService securityContextService;

    @CreateProjectDoc
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectResponse> create(@RequestBody @Valid ProjectRequest request) {
        TokenPayload payload = securityContextService.getAuthenticatedPayload();
        ProjectResponse response = createService.execute(payload.externalId(), request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.externalId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }
}
