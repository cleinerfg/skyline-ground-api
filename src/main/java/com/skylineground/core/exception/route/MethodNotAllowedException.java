package com.skylineground.core.exception.route;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;

public class MethodNotAllowedException extends AppException {
    public MethodNotAllowedException() {
        super(ErrorCode.METHOD_NOT_ALLOWED, "Method not allowed");
    }
}
