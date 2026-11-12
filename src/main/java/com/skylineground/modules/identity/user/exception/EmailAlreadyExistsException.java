package com.skylineground.modules.identity.user.exception;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;

public class EmailAlreadyExistsException extends AppException {
    public EmailAlreadyExistsException(String email) {
        super(ErrorCode.EMAIL_ALREADY_EXISTS,
                String.format("The email '%s' is already registered. Try signing in.", email)
        );
    }
}
