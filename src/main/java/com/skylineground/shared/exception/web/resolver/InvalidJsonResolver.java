package com.skylineground.shared.exception.web.resolver;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.exception.web.resolver.schema.FieldFormatViolation;
import com.skylineground.shared.exception.web.resolver.support.FieldFormatViolationExtractor;
import com.skylineground.shared.exception.web.support.ProblemDetailFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvalidJsonResolver {

    public static final String VIOLATION_SYNTAX = "SYNTAX";
    public static final String VIOLATION_FORMAT = "FORMAT";

    private final ProblemDetailFactory problemDetailFactory;

    public ProblemDetail resolve(HttpMessageNotReadableException ex) {
        var invalidJson = AppException.invalidJson();
        var problemDetail = problemDetailFactory.create(
                invalidJson.getErrorCode(),
                invalidJson.getMessage()
        );

        var formatViolation = extractFieldFormatViolation(ex);
        String violationType = VIOLATION_SYNTAX;

        if (formatViolation != null) {
            problemDetail.setProperty("fail", formatViolation);
            violationType = VIOLATION_FORMAT;
        }

        problemDetail.setProperty("violation", violationType);

        return problemDetail;
    }

    private FieldFormatViolation extractFieldFormatViolation(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException formatException) {
            Class<?> type = formatException.getTargetType();
            String field = formatException.getPath().isEmpty() ? "UNKNOWN_FIELD"
                    : formatException.getPath().getFirst().getFieldName();

            return FieldFormatViolation.builder()
                    .field(field)
                    .sentValue(String.valueOf(formatException.getValue()))
                    .expectedType(type.getSimpleName())
                    .acceptedValues(FieldFormatViolationExtractor.extractEnumConstants(type))
                    .message(FieldFormatViolationExtractor.extractMessage(type))
                    .build();
        }
        return null;
    }
}
