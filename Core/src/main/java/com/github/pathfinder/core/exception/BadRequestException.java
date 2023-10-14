package com.github.pathfinder.core.exception;

public class BadRequestException extends ServiceException {

    public BadRequestException(String message, Object... params) {
        super(ErrorCode.BAD_REQUEST, message, params);
    }

}
