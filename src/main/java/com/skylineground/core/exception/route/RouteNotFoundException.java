package com.skylineground.core.exception.route;

import com.skylineground.core.exception.AppException;
import com.skylineground.core.exception.ErrorCode;

public class RouteNotFoundException extends AppException {
    public RouteNotFoundException() {
        super(ErrorCode.ROUTE_NOT_FOUND, "The requested resource does not exist");
    }
}
