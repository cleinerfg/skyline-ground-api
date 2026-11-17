package com.skylineground.modules.identity.user.exception;

import com.skylineground.modules.identity.user.validator.PasswordError;
import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.exception.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class InvalidPasswordException extends AppException {

    private final Set<String> fails = new LinkedHashSet<>();

    public InvalidPasswordException(Set<PasswordError> fails) {
        super(ErrorCode.INVALID_PASSWORD, "The password provided is invalid.");

        this.fails.addAll(fails.stream()
                .map(PasswordError::name)
                .toList());
    }
}
