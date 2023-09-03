package com.github.pathfinder.messaging.converter;

import com.github.pathfinder.core.exception.ServiceException;
import com.github.pathfinder.messaging.error.ErrorMessage;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoreMessageConverter implements MessageConverter {

    private final Jackson2JsonMessageConverter messageConverter;

    @NotNull
    @Override
    public Message toMessage(@NotNull Object object, @NotNull MessageProperties messageProperties)
            throws MessageConversionException {
        return messageConverter.toMessage(object, messageProperties);
    }

    @NotNull
    @Override
    public Object fromMessage(@NotNull Message message) throws MessageConversionException {
        if (isError(message)) {
            log.debug("Received a error message, throwing an exception");
            throw exception(message);
        }

        log.debug("Received a non-error message, converting it");

        return messageConverter.fromMessage(message);
    }

    private RuntimeException exception(Message message) {
        var errorMessage = (ErrorMessage) messageConverter.fromMessage(message, ErrorMessage.class);

        throw new ServiceException(errorMessage.code(), errorMessage.reason(), errorMessage.message());
    }

    private boolean isError(Message message) {
        return isErrorType(message.getMessageProperties().getHeader(MessageHeaders.TYPE_ID));
    }

    private boolean isErrorType(String typeId) {
        return StringUtils.isNotEmpty(typeId) && typeId.equals(ErrorMessage.class.getName());
    }

}
