package com.skylineground.shared.exception.web.resolver;

import com.skylineground.shared.exception.route.MethodNotAllowedException;
import com.skylineground.shared.exception.web.support.ProblemDetailFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodNotAllowedResolver {

    private final ProblemDetailFactory problemDetailFactory;

    public ProblemDetail resolve() {
        var ex = new MethodNotAllowedException();
        return problemDetailFactory.create(
                ex.getErrorCode(),
                ex.getMessage()
        );
    }
}
