package com.skylineground.shared.exception.route;

import com.skylineground.shared.exception.AppException;
import com.skylineground.shared.exception.ErrorCode;

public class RouteNotFoundException extends AppException {
    public RouteNotFoundException() {
        super(ErrorCode.ROUTE_NOT_FOUND, "The requested resource does not exist");
    }
}
