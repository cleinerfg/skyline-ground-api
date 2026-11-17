package com.skylineground.shared.security.exception;

import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.exception.ErrorCode;

public class UnauthorizedAccessException extends AppException {
    public UnauthorizedAccessException() {
        super(ErrorCode.ACCESS_DENIED, "Access Denied");
    }
}
