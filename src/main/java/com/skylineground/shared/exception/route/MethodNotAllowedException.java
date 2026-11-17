package com.skylineground.shared.exception.route;

import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.exception.ErrorCode;

public class MethodNotAllowedException extends AppException {
    public MethodNotAllowedException() {
        super(ErrorCode.METHOD_NOT_ALLOWED, "Method not allowed");
    }
}
