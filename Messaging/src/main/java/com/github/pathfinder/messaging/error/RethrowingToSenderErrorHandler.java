package com.github.pathfinder.messaging.error;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.exception.ErrorCode;
import com.github.pathfinder.core.exception.ErrorReason;
import com.github.pathfinder.core.exception.ServiceException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Slf4j
@Component(ErrorHandlers.RETHROWING_HANDLER)
public class RethrowingToSenderErrorHandler implements RabbitListenerErrorHandler {

    @Override
    @Logged(ignoreReturnValue = false)
    public Object handleError(Message amqpMessage,
                              org.springframework.messaging.Message<?> message,
                              ListenerExecutionFailedException exception) {
        return cause(exception)
                .map(this::mapException)
                .orElseGet(() -> internalServerError("Failed to process request"));
    }

    private Optional<Throwable> cause(Throwable exception) {
        return Optional.ofNullable(exception.getCause());
    }

    private ErrorMessage internalServerError(String message) {
        return new ErrorMessage(ErrorCode.INTERNAL_SERVER_ERROR,
                                ErrorReason.INTERNAL_SERVER_ERROR.name(),
                                message);
    }

    private ErrorMessage mapException(Throwable exception) {
        if (exception instanceof ServiceException serviceException) {
            return message(serviceException);
        } else {
            return message(exception);
        }
    }

    private ErrorMessage message(ServiceException serviceException) {
        return new ErrorMessage(serviceException.errorCode(),
                                serviceException.reason(),
                                serviceException.getMessage());
    }

    private ErrorMessage message(Throwable exception) {
        return internalServerError(exception.getMessage());
    }

}
