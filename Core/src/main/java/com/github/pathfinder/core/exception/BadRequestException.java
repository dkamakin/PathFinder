package com.github.pathfinder.core.exception;

public class BadRequestException extends ServiceException {

    public BadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }

}
