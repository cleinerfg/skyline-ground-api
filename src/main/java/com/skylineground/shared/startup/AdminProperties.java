package com.skylineground.shared.startup;

import com.skylineground.modules.identity.user.exception.InvalidPasswordException;
import com.skylineground.modules.identity.user.validator.PasswordValidator;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.admin")
public record AdminProperties(
        String name,
        String email,
        String password
) {

    public AdminProperties {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Admin name must not be null or blank."
            );
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Admin email must not be null or blank."
            );
        }

        try {
            PasswordValidator.validate(password);
        } catch (InvalidPasswordException ex) {
            throw new IllegalArgumentException(
                    "Configured admin password is invalid.",
                    ex
            );
        }
    }
}
