package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppExceptionResolver {

    private final ProblemDetailFactory problemDetailFactory;

    public ProblemDetail resolve(AppException ex) {
        return problemDetailFactory.create(ex.getErrorCode(), ex.getMessage());
    }
}
