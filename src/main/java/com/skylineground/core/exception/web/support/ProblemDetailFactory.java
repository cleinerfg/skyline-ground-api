package com.skylineground.core.exception.web.support;

import com.skylineground.core.exception.ErrorCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
public class ProblemDetailFactory {

    public ProblemDetail create(ErrorCode errorCode, String message) {
        var problemDetail = ProblemDetail.forStatusAndDetail(errorCode.getStatus(), message);
        problemDetail.setTitle(errorCode.name());
        problemDetail.setProperty("category", errorCode.getCategory());
        return problemDetail;
    }
}
