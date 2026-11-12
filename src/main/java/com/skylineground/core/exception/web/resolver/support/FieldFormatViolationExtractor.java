package com.skylineground.core.exception.web.resolver.support;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class FieldFormatViolationExtractor {

    public static Set<String> extractEnumConstants(Class<?> type) {
        if (type != null && type.isEnum()) {
            return Stream.of(type.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public static String extractMessage(Class<?> type) {
        if (type == null)
            return "The provided data is not compatible with the expected type.";
        if (type.equals(OffsetDateTime.class))
            return "Use the ISO-8601 standard (e.g., 2026-03-24T14:30:00Z).";
        if (type.equals(LocalDate.class))
            return "Use the ISO-8601 standard for date (e.g., 2026-03-24).";
        if (type.equals(UUID.class))
            return "Use the UUID standard format (e.g., 123e4567-e89b-12d3-a456-426614174000).";
        if (Number.class.isAssignableFrom(type))
            return "The value must be a valid number.";

        return "The provided data is not compatible with the expected type.";
    }
}
