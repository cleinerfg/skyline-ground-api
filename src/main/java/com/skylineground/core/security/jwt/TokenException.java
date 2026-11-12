package com.skylineground.core.security.jwt;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;

public class TokenException extends AppException {

    private TokenException(ErrorCode errorCode) {
        super(errorCode, "Provide a valid Bearer Token.");
    }

    public static TokenException required() {
        return new TokenException(ErrorCode.TOKEN_REQUIRED);
    }

    public static TokenException invalid() {
        return new TokenException(ErrorCode.TOKEN_INVALID);
    }

    public static TokenException expired() {
        return new TokenException(ErrorCode.TOKEN_EXPIRED);
    }
}
