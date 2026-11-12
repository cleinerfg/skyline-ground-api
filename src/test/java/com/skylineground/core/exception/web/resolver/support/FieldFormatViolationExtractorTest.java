package com.skylineground.core.exception.web.resolver.support;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FieldFormatViolationExtractorTest {

    private enum TestStatus {
        ACTIVE,
        INACTIVE
    }

    @Test
    void shouldExtractEnumConstants() {
        Set<String> result =
                FieldFormatViolationExtractor.extractEnumConstants(TestStatus.class);

        assertThat(result)
                .containsExactlyInAnyOrder("ACTIVE", "INACTIVE");
    }

    @Test
    void shouldReturnEmptySetWhenTypeIsNotEnum() {
        Set<String> result =
                FieldFormatViolationExtractor.extractEnumConstants(String.class);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptySetWhenTypeIsNull() {
        Set<String> result =
                FieldFormatViolationExtractor.extractEnumConstants(null);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldExtractOffsetDateTimeMessage() {
        String result =
                FieldFormatViolationExtractor.extractMessage(OffsetDateTime.class);

        assertThat(result)
                .isEqualTo("Use the ISO-8601 standard (e.g., 2026-03-24T14:30:00Z).");
    }

    @Test
    void shouldExtractLocalDateMessage() {
        String result =
                FieldFormatViolationExtractor.extractMessage(LocalDate.class);

        assertThat(result)
                .isEqualTo("Use the ISO-8601 standard for date (e.g., 2026-03-24).");
    }

    @Test
    void shouldExtractUUIDMessage() {
        String result =
                FieldFormatViolationExtractor.extractMessage(UUID.class);

        assertThat(result)
                .isEqualTo("Use the UUID standard format (e.g., 123e4567-e89b-12d3-a456-426614174000).");
    }

    @Test
    void shouldExtractNumberMessage() {
        String result =
                FieldFormatViolationExtractor.extractMessage(BigDecimal.class);

        assertThat(result)
                .isEqualTo("The value must be a valid number.");
    }

    @Test
    void shouldReturnGenericMessageWhenTypeIsUnsupported() {
        String result =
                FieldFormatViolationExtractor.extractMessage(String.class);

        assertThat(result)
                .isEqualTo("The provided data is not compatible with the expected type.");
    }

    @Test
    void shouldReturnGenericMessageWhenTypeIsNull() {
        String result =
                FieldFormatViolationExtractor.extractMessage(null);

        assertThat(result)
                .isEqualTo("The provided data is not compatible with the expected type.");
    }
}
