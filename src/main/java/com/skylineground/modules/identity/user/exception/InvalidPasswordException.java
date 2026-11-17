package com.skylineground.modules.identity.user.exception;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;
import com.skylineground.modules.identity.user.validator.PasswordError;
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
