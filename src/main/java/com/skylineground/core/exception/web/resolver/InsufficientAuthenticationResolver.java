package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import com.skylineground.core.security.jwt.TokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InsufficientAuthenticationResolver {

    private final ProblemDetailFactory problemDetailFactory;

    public ProblemDetail resolve() {
        var tokenException = TokenException.required();
        return problemDetailFactory.create(tokenException.getErrorCode(), tokenException.getMessage());
    }
}
