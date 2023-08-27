package com.github.pathfinder.messaging.exception;

import com.github.pathfinder.core.exception.ErrorCode;
import com.github.pathfinder.core.exception.ServiceException;

public class MessagingException extends ServiceException {

    public MessagingException(String reason, String message, Object... params) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, reason, message, params);
    }
}
