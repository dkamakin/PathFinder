package com.github.pathfinder.messaging.exception;

import com.github.pathfinder.core.exception.InternalServerException;
import java.io.Serial;

public class MessageRoutingException extends InternalServerException {

    @Serial
    private static final long serialVersionUID = 6893842200907317317L;

    public MessageRoutingException() {
        super("Failed to route the message to the destination");
    }

}
