package com.github.pathfinder.core.exception;

public class ConflictException extends ServiceException {

    public ConflictException(String message, Object... parameters) {
        super(ErrorCode.CONFLICT, message, parameters);
    }

}
