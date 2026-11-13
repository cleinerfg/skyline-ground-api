package com.skylineground.modules.project.exception;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;

public class ProjectNameAlreadyExistsException extends AppException {

    public ProjectNameAlreadyExistsException(String projectName) {
        super(ErrorCode.PROJECT_NAME_ALREADY_EXISTS,
                "A project cannot exist with an already registered name: '%s'".formatted(projectName)
        );
    }
}
