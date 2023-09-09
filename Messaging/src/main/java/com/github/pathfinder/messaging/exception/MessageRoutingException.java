package com.github.pathfinder.messaging.exception;

import com.github.pathfinder.core.exception.InternalServerException;

public class MessageRoutingException extends InternalServerException {

    public MessageRoutingException() {
        super("Failed to route the message to the destination");
    }
}
