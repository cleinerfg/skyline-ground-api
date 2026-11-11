package com.skylineground.modules.identity.auth.doc;

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
        summary = "Register new user",
        description = "Creates a new user account in the system and returns user details."
)
@ApiResponse(responseCode = "201", description = "User successfully registered")
@ApiResponse(responseCode = "400", description = "Invalid request payload or validation failed", content = @Content)
@ApiResponse(responseCode = "409", description = "Email or username already registered", content = @Content)
public @interface RegisterUserDoc {
}
