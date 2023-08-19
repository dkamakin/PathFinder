package com.github.pathfinder.core.exception;

public class ServiceNotFoundException extends ServiceException {

    public ServiceNotFoundException(String message, Object... params) {
        super(ErrorCode.NOT_FOUND, ErrorReason.NOT_FOUND.name(), message, params);
    }
}
