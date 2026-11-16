package com.skylineground.modules.identity.user.validator;

import com.skylineground.modules.identity.user.exception.InvalidPasswordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordValidatorTest {

    @ParameterizedTest
    @MethodSource("validPasswordCases")
    void shouldValidatePasswordSuccessfully(String password) {
        PasswordValidator.validate(password);
    }

    private static Stream<String> validPasswordCases() {
        return Stream.of(
                "Secure@389x",   // Valid password
                "Secure@Pass9",  // Allows special characters
                "Secure@!#Pass9", // Ignores special characters in sequence validation
                "Secure1a1@"     // Allows non-consecutive repeated characters
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordCases")
    void shouldThrowExceptionWhenPasswordDoesNotMeetRequirements(
            String password,
            PasswordError expectedError
    ) {
        assertPasswordFails(password, expectedError);
    }

    private static Stream<Arguments> invalidPasswordCases() {
        return Stream.of(
                Arguments.of("Ab1@", PasswordError.TOO_SHORT),
                Arguments.of("Secure 3@", PasswordError.NO_WHITESPACE),
                Arguments.of("secure173@", PasswordError.MUST_HAVE_UPPERCASE),
                Arguments.of("SECURE193@", PasswordError.MUST_HAVE_LOWERCASE),
                Arguments.of("SecurePass@", PasswordError.MUST_HAVE_NUMBER),
                Arguments.of("Secure173", PasswordError.MUST_HAVE_SPECIAL_CHARACTER),
                Arguments.of("Secure123@", PasswordError.NO_SEQUENTIAL_CHARACTER),
                Arguments.of("Secure321@", PasswordError.NO_SEQUENTIAL_CHARACTER),
                Arguments.of("Secureaaa1@", PasswordError.NO_REPEATING_CHARACTER),
                Arguments.of("Secure111@", PasswordError.NO_REPEATING_CHARACTER)
        );
    }

    @Test
    void shouldThrowExceptionWhenPasswordContainsMultipleInvalidConditions() {
        assertPasswordFails(
                "abc 123",
                PasswordError.TOO_SHORT,
                PasswordError.NO_WHITESPACE,
                PasswordError.MUST_HAVE_UPPERCASE,
                PasswordError.MUST_HAVE_SPECIAL_CHARACTER,
                PasswordError.NO_SEQUENTIAL_CHARACTER
        );
    }

    private void assertPasswordFails(
            String password,
            PasswordError... expectedErrors
    ) {
        assertThatThrownBy(() -> PasswordValidator.validate(password))
                .isInstanceOf(InvalidPasswordException.class)
                .satisfies(exception -> {
                    InvalidPasswordException invalidPasswordException =
                            (InvalidPasswordException) exception;

                    assertThat(invalidPasswordException.getFails())
                            .containsExactlyInAnyOrderElementsOf(
                                    Set.of(expectedErrors).stream()
                                            .map(Enum::name)
                                            .toList()
                            );
                });
    }
}
