package com.github.pathfinder.security.api.configuration;

import com.github.pathfinder.messaging.configuration.MessagingConfiguration;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Getter
@ToString
@Validated
@RefreshScope
@Configuration
@EqualsAndHashCode
@Import(MessagingConfiguration.class)
public class SecurityMessagingConfiguration {

    @UtilityClass
    public static class Token {

        public static final String DEFAULT_QUEUE_NAME = "${queue.security.default.name}";
    }

    @NotBlank
    @Value(Token.DEFAULT_QUEUE_NAME)
    private String defaultQueueName;

    @Bean
    public Queue securityDefaultQueue() {
        return QueueBuilder.durable(defaultQueueName).build();
    }

    @Bean
    public Binding securityBinding(DirectExchange directExchange) {
        log.info("Security binding with configuration: {}", this);
        return BindingBuilder
                .bind(securityDefaultQueue())
                .to(directExchange)
                .withQueueName();
    }

}
