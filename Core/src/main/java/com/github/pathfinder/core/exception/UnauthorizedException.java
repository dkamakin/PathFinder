package com.github.pathfinder.core.exception;

public class UnauthorizedException extends ServiceException {

    public UnauthorizedException(String message, Object... parameters) {
        super(ErrorCode.UNAUTHORIZED, message, parameters);
    }

}
