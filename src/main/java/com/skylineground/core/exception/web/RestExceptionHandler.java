package com.skylineground.core.exception.web;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.web.resolver.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RequiredArgsConstructor
@RestControllerAdvice
public class RestExceptionHandler {

    private final InvalidJsonResolver invalidJsonResolver;
    private final InvalidParameterResolver invalidParameterResolver;
    private final ValidationFailureResolver validationFailureResolver;
    private final RouteNotFoundResolver routeNotFoundResolver;
    private final MethodNotAllowedResolver methodNotAllowedResolver;
    private final AppExceptionResolver appExceptionResolver;
    private final InsufficientAuthenticationResolver insufficientAuthenticationResolver;
    private final UnexpectedExceptionResolver unexpectedExceptionResolver;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleInvalidJson(HttpMessageNotReadableException ex) {
        return invalidJsonResolver.resolve(ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleInvalidParameter(MethodArgumentTypeMismatchException ex) {
        return invalidParameterResolver.resolve(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationFailure(MethodArgumentNotValidException ex) {
        return validationFailureResolver.resolve(ex);
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ProblemDetail handleRouteNotFound() {
        return routeNotFoundResolver.resolve();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotAllowed() {
        return methodNotAllowedResolver.resolve();
    }

    @ExceptionHandler(AppException.class)
    public ProblemDetail handleAppException(AppException ex) {
        return appExceptionResolver.resolve(ex);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ProblemDetail handleInsufficientAuthentication() {
        return insufficientAuthenticationResolver.resolve();
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception ex) {
        return unexpectedExceptionResolver.resolve(ex);
    }
}
