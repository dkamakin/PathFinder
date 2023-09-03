package com.github.pathfinder.messaging.exception;

public class MessageRoutingException extends MessagingException {

    public MessageRoutingException() {
        super(ErrorReason.FAILED_TO_ROUTE.name(), "Failed to route the message to the destination");
    }
}
