package com.skylineground.modules.project.exception;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ProjectAlreadyExistsException extends AppException {

    private final UUID existingProjectId;

    public ProjectAlreadyExistsException(UUID existingProjectId) {

        super(ErrorCode.PROJECT_ALREADY_EXISTS,
                "A project with the same details already exists with externalId: " + existingProjectId
        );
        this.existingProjectId = existingProjectId;
    }
}
