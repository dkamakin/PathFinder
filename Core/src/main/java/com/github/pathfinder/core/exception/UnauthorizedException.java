package com.github.pathfinder.core.exception;

public class UnauthorizedException extends ServiceException {

    public UnauthorizedException(String reason, String message, Object... params) {
        super(ErrorCode.UNAUTHORIZED, reason, message, params);
    }
}
