package com.github.pathfinder.core.exception;

public class ConflictException extends ServiceException {

    public ConflictException(String reason, String message, Object... params) {
        super(ErrorCode.CONFLICT, reason, message, params);
    }
}
