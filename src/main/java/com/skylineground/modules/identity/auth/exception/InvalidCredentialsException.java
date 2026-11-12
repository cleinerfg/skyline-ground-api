package com.skylineground.modules.identity.auth.exception;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;

public class InvalidCredentialsException extends AppException {

    public InvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS, "Incorrect email or password.");
    }
}
