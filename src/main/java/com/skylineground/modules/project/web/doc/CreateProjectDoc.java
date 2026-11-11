package com.skylineground.modules.project.web.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Create new project",
        description = "Creates a new project in the system and returns its details along with the resource location URI."
)
@ApiResponse(responseCode = "201", description = "Project successfully created")
@ApiResponse(responseCode = "400", description = "Invalid request payload or validation failed", content = @Content)
public @interface CreateProjectDoc {
}
