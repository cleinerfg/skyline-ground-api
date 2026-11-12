package com.skylineground.core.exception.web.resolver.handler;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.web.resolver.schema.FieldValidationViolation;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationFailHandlerTest {

    @Mock
    private ProblemDetailFactory problemDetailFactory;

    @InjectMocks
    private ValidationFailHandler handler;

    @Test
    void shouldGroupValidationErrorsByField() {
        var exception = AppException.validationFail();

        var fieldErrors = List.of(
                new FieldError("request", "name", " ", false, null, null, "Name is required"),
                new FieldError("request", "name", " ", false, null, null, "Name must not be blank"),
                new FieldError("request", "email", "invalid", false, null, null, "Invalid email")
        );

        var expected = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        when(problemDetailFactory.create(
                exception.getErrorCode(),
                exception.getMessage()
        )).thenReturn(expected);

        var result = handler.handle(fieldErrors);

        assertThat(result).isSameAs(expected);

        var fails = assertThat(result.getProperties())
                .extracting("fails", list(FieldValidationViolation.class))
                .actual();

        assertThat(fails).hasSize(2);

        assertThat(fails)
                .extracting(FieldValidationViolation::field)
                .containsExactlyInAnyOrder("name", "email");

        var nameViolation = fails.stream()
                .filter(violation -> violation.field().equals("name"))
                .findFirst()
                .orElseThrow();

        assertThat(nameViolation.errors())
                .containsExactlyInAnyOrder(
                        "Name is required",
                        "Name must not be blank"
                );

        verify(problemDetailFactory).create(
                exception.getErrorCode(),
                exception.getMessage()
        );
    }

    @Test
    void shouldReturnEmptyViolationsWhenNoFieldErrors() {
        var exception = AppException.validationFail();
        var expected = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        when(problemDetailFactory.create(
                exception.getErrorCode(),
                exception.getMessage()
        )).thenReturn(expected);

        var result = handler.handle(List.of());

        assertThat(result).isSameAs(expected);
        assertThat(result.getProperties())
                .containsEntry("fails", List.of());

        verify(problemDetailFactory).create(
                exception.getErrorCode(),
                exception.getMessage()
        );
    }
}
