package com.github.pathfinder.security.api.configuration;

import com.github.pathfinder.messaging.configuration.MessagingConfiguration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MessagingConfiguration.class)
public class SecurityMessagingConfiguration {

    @Bean
    public Queue securityApiQueue(SecurityQueueConfiguration queueConfiguration) {
        return new Queue(queueConfiguration.getQueueName(), false, false, true);
    }

    @Bean
    public Binding directExchangeBinding(Queue securityApiQueue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(securityApiQueue)
                .to(directExchange)
                .withQueueName();
    }

}
