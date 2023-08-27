package com.github.pathfinder.messaging.exception;

public class ReceiveMessageException extends MessagingException {

    public ReceiveMessageException(String queue) {
        super(ErrorReason.FAILED_TO_RECEIVE.name(), "Failed to receive a response from the queue '%s'", queue);
    }
}
