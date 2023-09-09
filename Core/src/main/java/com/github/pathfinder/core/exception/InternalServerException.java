package com.github.pathfinder.core.exception;

public class InternalServerException extends ServiceException {

    public InternalServerException(String message, Object... parameters) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message, parameters);
    }

}
