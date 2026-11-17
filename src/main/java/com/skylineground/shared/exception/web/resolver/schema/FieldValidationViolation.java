package com.skylineground.shared.exception.web.resolver.schema;

import java.util.Set;

public record FieldValidationViolation(
        String field,
        Set<String> errors
) {
}
