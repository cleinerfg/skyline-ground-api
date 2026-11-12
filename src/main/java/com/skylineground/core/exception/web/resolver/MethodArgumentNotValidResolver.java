package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.web.resolver.handler.InvalidQueryParamsHandler;
import com.skylineground.core.exception.web.resolver.handler.ValidationFailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RequiredArgsConstructor
@Component
public class MethodArgumentNotValidResolver {

    private final ValidationFailHandler validationFailHandler;
    private final InvalidQueryParamsHandler invalidQueryParamsHandler;

    public ProblemDetail resolve(MethodArgumentNotValidException ex) {
        var fieldErrors = ex.getBindingResult().getFieldErrors();

        var queryParamsProblem = invalidQueryParamsHandler.handle(fieldErrors);

        return queryParamsProblem != null
                ? queryParamsProblem
                : validationFailHandler.handle(fieldErrors);
    }
}
