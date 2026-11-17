package com.skylineground.shared.startup;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminPropertiesTest {

    private static final String NAME = "Admin";
    private static final String EMAIL = "admin@example.com";
    private static final String VALID_PASSWORD = "V7!mQ2#rT9";

    @Test
    void shouldCreatePropertiesWhenAllValuesAreValid() {
        AdminProperties props = new AdminProperties(
                NAME,
                EMAIL,
                VALID_PASSWORD
        );

        assertThat(props.name()).isEqualTo(NAME);
        assertThat(props.email()).isEqualTo(EMAIL);
        assertThat(props.password()).isEqualTo(VALID_PASSWORD);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> new AdminProperties(
                null,
                EMAIL,
                VALID_PASSWORD
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Admin name must not be null or blank.");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNameIsBlank() {
        assertThatThrownBy(() -> new AdminProperties(
                "   ",
                EMAIL,
                VALID_PASSWORD
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Admin name must not be null or blank.");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenEmailIsNull() {
        assertThatThrownBy(() -> new AdminProperties(
                NAME,
                null,
                VALID_PASSWORD
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Admin email must not be null or blank.");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenEmailIsBlank() {
        assertThatThrownBy(() -> new AdminProperties(
                NAME,
                "   ",
                VALID_PASSWORD
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Admin email must not be null or blank.");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenPasswordIsInvalid() {
        assertThatThrownBy(() -> new AdminProperties(
                NAME,
                EMAIL,
                "weak"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Configured admin password is invalid.")
                .hasCauseInstanceOf(RuntimeException.class);
    }
}
