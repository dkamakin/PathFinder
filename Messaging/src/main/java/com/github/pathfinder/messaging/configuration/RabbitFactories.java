package com.github.pathfinder.messaging.configuration;

import lombok.experimental.UtilityClass;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;

@UtilityClass
public class RabbitFactories {

    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> listenerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter, int concurrentConsumers) {
        var factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setMaxConcurrentConsumers(concurrentConsumers);
        factory.setDefaultRequeueRejected(false);

        return factory;
    }

}
