package com.github.pathfinder.core.exception;

public class ForbiddenException extends ServiceException {

    public ForbiddenException(String reason, String message, Object... params) {
        super(ErrorCode.FORBIDDEN, reason, message, params);
    }
}
