package com.github.pathfinder.core.exception;

public class NotFoundException extends ServiceException {

    public NotFoundException(String message, Object... params) {
        super(ErrorCode.NOT_FOUND, ErrorReason.NOT_FOUND.name(), message, params);
    }
}
