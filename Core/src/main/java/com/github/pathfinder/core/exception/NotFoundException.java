package com.github.pathfinder.core.exception;

public class NotFoundException extends ServiceException {

    public NotFoundException(String message, Object... parameters) {
        super(ErrorCode.NOT_FOUND, message, parameters);
    }

}
