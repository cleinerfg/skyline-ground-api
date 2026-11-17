package com.skylineground.shared.exception.web.resolver;

import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.exception.web.support.ProblemDetailFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnexpectedExceptionResolver {

    private final ProblemDetailFactory problemDetailFactory;

    public ProblemDetail resolve(Exception ex) {
        var appException = AppException.systemIntegrity(ex);
        return problemDetailFactory.create(appException.getErrorCode(), appException.getMessage());
    }
}
