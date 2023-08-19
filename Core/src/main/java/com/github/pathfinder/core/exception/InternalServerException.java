package com.github.pathfinder.core.exception;

public class InternalServerException extends ServiceException {

    public InternalServerException(String reason, String message, Object... params) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, reason, message, params);
    }
}
