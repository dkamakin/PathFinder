package com.github.pathfinder.core.exception;

public class BadRequestException extends ServiceException {

    public BadRequestException(String reason, String message, Object... params) {
        super(ErrorCode.BAD_REQUEST, reason, message, params);
    }
}
