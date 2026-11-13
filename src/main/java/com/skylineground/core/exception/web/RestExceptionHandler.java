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
    private final InvalidParamResolver invalidParamResolver;
    private final MethodArgumentNotValidResolver methodArgumentNotValidResolver;
    private final RouteNotFoundResolver routeNotFoundResolver;
    private final MethodNotAllowedResolver methodNotAllowedResolver;
    private final AppExceptionResolver appExceptionResolver;
    private final InsufficientAuthenticationResolver insufficientAuthenticationResolver;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return invalidJsonResolver.resolve(ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return invalidParamResolver.resolve(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return methodArgumentNotValidResolver.resolve(ex);
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ProblemDetail handleRouteNotFound() {
        return routeNotFoundResolver.resolve();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleHttpRequestMethodNotSupported() {
        return methodNotAllowedResolver.resolve();
    }

    @ExceptionHandler(AppException.class)
    public ProblemDetail handleAppException(AppException ex) {
        return appExceptionResolver.resolve(ex);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ProblemDetail handleInsufficientAuthenticationException() {
        return insufficientAuthenticationResolver.resolve();
    }
}
