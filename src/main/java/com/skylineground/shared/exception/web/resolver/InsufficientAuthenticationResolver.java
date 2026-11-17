package com.skylineground.shared.exception.web.resolver;

import com.skylineground.shared.exception.web.support.ProblemDetailFactory;
import com.skylineground.shared.security.jwt.TokenException;
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
