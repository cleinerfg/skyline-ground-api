package com.skylineground.core.exception.web.resolver.handler;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.web.resolver.schema.FieldFormatViolation;
import com.skylineground.core.exception.web.resolver.support.FieldFormatViolationExtractor;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvalidQueryParamsHandler {

    private final ProblemDetailFactory problemDetailFactory;

    public ProblemDetail handle(List<FieldError> fieldErrors) {
        var typeMismatchErrors = fieldErrors.stream()
                .filter(this::hasCodeTypeMismatch)
                .toList();

        if (typeMismatchErrors.isEmpty()) return null;

        var invalidParam = AppException.invalidQueryParams();
        var problemDetail = problemDetailFactory.create(
                invalidParam.getErrorCode(),
                invalidParam.getMessage()
        );

        var violations = typeMismatchErrors.stream()
                .map(this::toViolation)
                .toList();

        problemDetail.setProperty("fails", violations);
        return problemDetail;
    }

    private FieldFormatViolation toViolation(FieldError error) {
        Class<?> requiredType = extractRequiredType(error);

        return FieldFormatViolation.builder()
                .field(error.getField())
                .sentValue(String.valueOf(error.getRejectedValue()))
                .expectedType(requiredType != null ? requiredType.getSimpleName() : "UNKNOWN")
                .acceptedValues(FieldFormatViolationExtractor.extractEnumConstants(requiredType))
                .message(FieldFormatViolationExtractor.extractMessage(requiredType))
                .build();
    }

    private Class<?> extractRequiredType(FieldError error) {
        return error.unwrap(TypeMismatchException.class).getRequiredType();
    }

    private boolean hasCodeTypeMismatch(FieldError error) {
        return error.getCodes() != null &&
                Arrays.asList(error.getCodes()).contains("typeMismatch");
    }
}
