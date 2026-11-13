package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.web.resolver.handler.InvalidQueryParamsHandler;
import com.skylineground.core.exception.web.resolver.handler.ValidationFailHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationFailureResolverTest {

    @Mock
    private ValidationFailHandler validationFailHandler;

    @Mock
    private InvalidQueryParamsHandler invalidQueryParamsHandler;

    @Mock
    private MethodArgumentNotValidException exception;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ValidationFailureResolver resolver;

    @Test
    void shouldResolveInvalidQueryParams() {
        var fieldErrors = List.of(
                new FieldError(
                        "request",
                        "page",
                        "invalid",
                        false,
                        null,
                        null,
                        "Invalid value"
                )
        );

        var expected = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(invalidQueryParamsHandler.handle(fieldErrors)).thenReturn(expected);

        var result = resolver.resolve(exception);

        assertThat(result).isSameAs(expected);

        verify(invalidQueryParamsHandler).handle(fieldErrors);
        verifyNoInteractions(validationFailHandler);
    }

    @Test
    void shouldResolveValidationFailuresWhenNoInvalidQueryParams() {
        var fieldErrors = List.of(
                new FieldError(
                        "request",
                        "name",
                        "",
                        false,
                        null,
                        null,
                        "Name is required"
                )
        );

        var expected = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(invalidQueryParamsHandler.handle(fieldErrors)).thenReturn(null);
        when(validationFailHandler.handle(fieldErrors)).thenReturn(expected);

        var result = resolver.resolve(exception);

        assertThat(result).isSameAs(expected);

        verify(invalidQueryParamsHandler).handle(fieldErrors);
        verify(validationFailHandler).handle(fieldErrors);
    }
}
