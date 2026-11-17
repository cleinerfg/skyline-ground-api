package com.skylineground.shared.exception.web.support;

import com.skylineground.shared.exception.ErrorCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ProblemDetailFactory {

    public ProblemDetail create(ErrorCode errorCode, String message) {
        var problemDetail = ProblemDetail.forStatusAndDetail(errorCode.getStatus(), message);
        problemDetail.setTitle(errorCode.name());
        problemDetail.setProperty("category", errorCode.getCategory());
        problemDetail.setProperty("timestamp", Instant.now().toString());
        return problemDetail;
    }
}
