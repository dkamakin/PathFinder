package com.github.pathfinder.messaging.error;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.exception.ErrorCode;
import com.github.pathfinder.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        log.error("Handling exception", exception);
        return mapException(cause(exception));
    }

    private Throwable cause(Throwable exception) {
        while (exception.getCause() != null) {
            exception = exception.getCause();
        }

        return exception;
    }

    private ErrorMessage mapException(Throwable exception) {
        if (exception instanceof ServiceException serviceException) {
            return mapException(serviceException);
        }

        return new ErrorMessage(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ErrorMessage mapException(ServiceException exception) {
        return new ErrorMessage(exception.errorCode(), exception.getMessage());
    }

}
