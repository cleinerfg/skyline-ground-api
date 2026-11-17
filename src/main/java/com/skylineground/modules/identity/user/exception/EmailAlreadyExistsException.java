package com.skylineground.modules.identity.user.exception;

import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.exception.ErrorCode;

public class EmailAlreadyExistsException extends AppException {
    public EmailAlreadyExistsException(String email) {
        super(ErrorCode.EMAIL_ALREADY_EXISTS,
                String.format("The email '%s' is already registered. Try signing in.", email)
        );
    }
}
