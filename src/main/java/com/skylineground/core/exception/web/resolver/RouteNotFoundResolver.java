package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.route.RouteNotFoundException;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteNotFoundResolver {

    private final ProblemDetailFactory problemDetailFactory;

    public ProblemDetail resolve() {
        var ex = new RouteNotFoundException();
        return problemDetailFactory.create(
                ex.getErrorCode(),
                ex.getMessage()
        );
    }
}
