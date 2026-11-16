package com.skylineground.modules.project.exception;

import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectAlreadyExistsResolver {

    private final ProblemDetailFactory problemDetailFactory;

    public ProblemDetail resolve(ProjectAlreadyExistsException ex) {
        var problemDetail = problemDetailFactory.create(ex.getErrorCode(), ex.getMessage());

        problemDetail.setProperty("existingProjectId", ex.getExistingProjectId());

        return problemDetail;
    }
}
