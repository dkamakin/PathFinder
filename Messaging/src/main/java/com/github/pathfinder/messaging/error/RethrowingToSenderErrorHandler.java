package com.github.pathfinder.messaging.error;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.exception.ErrorCode;
import com.github.pathfinder.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;

@Slf4j
@Component(RethrowingToSenderErrorHandler.NAME)
public class RethrowingToSenderErrorHandler implements RabbitListenerErrorHandler {

    public static final String NAME = "rethrowingHandler";

    @Override
    @Logged(ignoreReturnValue = false)
    public Object handleError(Message amqpMessage,
                              org.springframework.messaging.Message<?> message,
                              ListenerExecutionFailedException exception) {
        return mapException(cause(exception));
    }

    private Throwable cause(Throwable exception) {
        while (exception.getCause() != null) {
            exception = exception.getCause();
        }

        log.error("Got exception cause", exception);

        return exception;
    }

    private ErrorMessage mapException(Throwable exception) {
        if (exception instanceof ServiceException serviceException) {
            return mapException(serviceException);
        } else if (exception instanceof MethodArgumentNotValidException ||
                exception instanceof IllegalArgumentException) {
            return new ErrorMessage(ErrorCode.BAD_REQUEST, "Service received an invalid request");
        }

        return new ErrorMessage(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ErrorMessage mapException(ServiceException exception) {
        return new ErrorMessage(exception.errorCode(), exception.getMessage());
    }

}
