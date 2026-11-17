package com.skylineground.modules.identity.user.validator;


import com.skylineground.modules.identity.user.exception.InvalidPasswordException;
import com.skylineground.shared.config.SystemConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PasswordValidator {

    public static void validate(String password) {

        Set<PasswordError> fails = new LinkedHashSet<>();

        if (password.length() < SystemConstant.PASSWORD_MIN_LENGTH) fails.add(PasswordError.TOO_SHORT);
        if (password.chars().anyMatch(Character::isWhitespace)) fails.add(PasswordError.NO_WHITESPACE);
        if (password.chars().noneMatch(Character::isUpperCase)) fails.add(PasswordError.MUST_HAVE_UPPERCASE);
        if (password.chars().noneMatch(Character::isLowerCase)) fails.add(PasswordError.MUST_HAVE_LOWERCASE);
        if (password.chars().noneMatch(Character::isDigit)) fails.add(PasswordError.MUST_HAVE_NUMBER);
        if (password.chars().noneMatch(c -> !Character.isLetterOrDigit(c) && !Character.isWhitespace(c))) {
            fails.add(PasswordError.MUST_HAVE_SPECIAL_CHARACTER);
        }

        checkSequenceAndRepetition(password, fails);

        if (!fails.isEmpty()) {
            throw new InvalidPasswordException(fails);
        }
    }

    private static void checkSequenceAndRepetition(String password, Set<PasswordError> fails) {
        if (password == null || password.length() < 3) return;

        boolean foundSequence = false;
        boolean foundRepetition = false;

        for (int i = 0; i < password.length() - 2; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);

            // Assign only if no match
            // Prevents erroneous reset to false
            if (!foundRepetition) {
                foundRepetition = checkRepetition(c1, c2, c3);
            }

            if (!foundSequence) {
                foundSequence = checkSequence(c1, c2, c3);
            }

            if (foundSequence && foundRepetition) break; // Optimization: early exit
        }

        if (foundRepetition) fails.add(PasswordError.NO_REPEATING_CHARACTER);
        if (foundSequence) fails.add(PasswordError.NO_SEQUENTIAL_CHARACTER);
    }

    private static boolean checkRepetition(char c1, char c2, char c3) {
        // Repetition (aaa, 111, DDD)
        return (c1 == c2 && c2 == c3);
    }

    private static boolean checkSequence(char c1, char c2, char c3) {
        // Convert chars to String to check against regex
        String triple = "" + c1 + c2 + c3;

        // Skip sequence validation if the triple contains special characters,
        // as users are typically unaware of their ASCII/Unicode order.
        if (triple.chars().anyMatch(c -> !Character.isLetterOrDigit(c) && !Character.isWhitespace(c))) {
            return false;
        }

        // Identifies character sequences by char arithmetic.
        // Since characters are represented as numeric Unicode values,
        // (c1 + 1 == c2) effectively checks if characters are adjacent
        // in the ASCII/Unicode table (e.g., 'a'+1='b' or '1'+1='2').

        // Detects both ascending (123, abc) and descending (321, cba)
        boolean isAscending = (c1 + 1 == c2 && c2 + 1 == c3);
        boolean isDescending = (c1 - 1 == c2 && c2 - 1 == c3);

        return (isAscending || isDescending);
    }
}
