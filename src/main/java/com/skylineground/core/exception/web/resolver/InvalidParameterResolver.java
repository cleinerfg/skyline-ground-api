package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.web.resolver.schema.FieldFormatViolation;
import com.skylineground.core.exception.web.resolver.support.FieldFormatViolationExtractor;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Component
@RequiredArgsConstructor
public class InvalidParameterResolver {

    private final ProblemDetailFactory problemDetailFactory;

    public ProblemDetail resolve(MethodArgumentTypeMismatchException ex) {
        var invalidParam = AppException.invalidParam();
        var problemDetail = problemDetailFactory.create(
                invalidParam.getErrorCode(),
                invalidParam.getMessage()
        );

        var violation = extractFieldFormatViolation(ex);
        problemDetail.setProperty("fail", violation);

        return problemDetail;
    }

    private FieldFormatViolation extractFieldFormatViolation(MethodArgumentTypeMismatchException ex) {
        Class<?> type = ex.getRequiredType();

        return FieldFormatViolation.builder()
                .field(ex.getName())
                .sentValue(String.valueOf(ex.getValue()))
                .expectedType(type != null ? type.getSimpleName() : "UNKNOWN")
                .acceptedValues(FieldFormatViolationExtractor.extractEnumConstants(type))
                .message(FieldFormatViolationExtractor.extractMessage(type))
                .build();
    }
}
