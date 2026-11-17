package com.skylineground.modules.identity.auth.exception;

import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.exception.ErrorCode;

public class InvalidCredentialsException extends AppException {

    public InvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS, "Incorrect email or password.");
    }
}
