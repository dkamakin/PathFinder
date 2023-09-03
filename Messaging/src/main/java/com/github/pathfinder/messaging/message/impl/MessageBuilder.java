package com.github.pathfinder.messaging.message.impl;

import com.github.pathfinder.messaging.configuration.AmqpConfiguration;
import com.github.pathfinder.messaging.message.IMessage;
import com.github.pathfinder.messaging.message.IMessageBuilder;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageBuilder implements IMessageBuilder {

    private final AmqpConfiguration amqpConfiguration;

    @Override
    public IRoutingKeySetter direct() {
        return routingKeySetter(AmqpConfiguration::getDirectExchangeName);
    }

    private IRoutingKeySetter routingKeySetter(Function<AmqpConfiguration, String> exchangeNameExtractor) {
        return new RoutingKeySetter(exchangeNameExtractor.apply(amqpConfiguration));
    }

    @RequiredArgsConstructor
    public static class RoutingKeySetter implements IRoutingKeySetter {

        private final String exchangeName;

        @Override
        public IMessageSetter routingKey(String routingKey) {
            return new MessageSetter(exchangeName, routingKey);
        }
    }

    @RequiredArgsConstructor
    public static class MessageSetter implements IMessageSetter {

        private final String exchangeName;
        private final String routingKey;

        @Override
        public <T> IMessage<T> with(T data) {
            return new Message<>(exchangeName, routingKey, data);
        }
    }
}
