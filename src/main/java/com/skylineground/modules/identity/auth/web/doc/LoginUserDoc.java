package com.skylineground.modules.identity.auth.web.doc;

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
        summary = "Authenticate user",
        description = "Authenticates user credentials and returns an access token."
)
@ApiResponse(responseCode = "200", description = "Authentication successful")
@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
public @interface LoginUserDoc {
}
