package com.skylineground.core.exception.web.resolver.schema;

import lombok.Builder;

import java.util.Set;

@Builder
public record FieldFormatViolation(
        String field,
        String sentValue,
        String expectedType,
        Set<String> acceptedValues,
        String message
) {
}
