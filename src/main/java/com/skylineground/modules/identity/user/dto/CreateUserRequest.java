package com.skylineground.modules.identity.user.dto;

import com.skylineground.shared.exception.ValidationMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateUserRequest(

        @NotBlank(message = ValidationMessage.REQUIRED)
        @Size(max = 255, message = ValidationMessage.MAX_LENGTH_255)
        String name,

        @NotBlank(message = ValidationMessage.REQUIRED)
        @Email(message = ValidationMessage.INVALID_EMAIL_FORMAT)
        @Size(max = 255, message = ValidationMessage.MAX_LENGTH_255)
        String email,

        @NotBlank(message = ValidationMessage.REQUIRED)
        @Size(max = 255, message = ValidationMessage.MAX_LENGTH_255)
        String password
) {
}
