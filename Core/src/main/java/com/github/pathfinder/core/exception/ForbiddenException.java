package com.github.pathfinder.core.exception;

public class ForbiddenException extends ServiceException {

    public ForbiddenException(String message, Object... parameters) {
        super(ErrorCode.FORBIDDEN, message, parameters);
    }

}
