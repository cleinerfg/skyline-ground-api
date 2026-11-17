package com.skylineground.modules.identity.user.exception;

import com.skylineground.shared.exception.web.support.ProblemDetailFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvalidPasswordResolver {

    private final ProblemDetailFactory problemDetailFactory;

    public ProblemDetail resolve(InvalidPasswordException ex) {
        var problemDetail = problemDetailFactory.create(ex.getErrorCode(), ex.getMessage());

        problemDetail.setProperty("fails", ex.getFails());

        return problemDetail;
    }
}
