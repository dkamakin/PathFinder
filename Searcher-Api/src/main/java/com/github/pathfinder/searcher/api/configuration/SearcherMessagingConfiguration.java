package com.github.pathfinder.searcher.api.configuration;

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
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class SearcherMessagingConfiguration {

    @UtilityClass
    public static class Token {

        public static final String DEFAULT_QUEUE_NAME     = "${queue.searcher.default.name}";
        public static final String DEAD_LETTER_QUEUE_NAME = "${queue.searcher.deadLetter.name}";

    }

    @NotBlank
    @Value(Token.DEFAULT_QUEUE_NAME)
    private String defaultQueueName;

    @NotBlank
    @Value(Token.DEAD_LETTER_QUEUE_NAME)
    private String deadLetterQueueName;

    @NotBlank
    @Value("${exchange.searcher.deadLetter.name}")
    private String deadLetterExchangeName;

    @Bean
    public Queue searcherDeadLetterQueue() {
        return QueueBuilder.durable(deadLetterQueueName).build();
    }

    @Bean
    public DirectExchange searcherDeadLetterExchange() {
        return ExchangeBuilder.directExchange(deadLetterExchangeName).build();
    }

    @Bean
    public Binding searcherDeadLetterBinding() {
        return BindingBuilder.bind(searcherDeadLetterQueue()).to(searcherDeadLetterExchange()).withQueueName();
    }

    @Bean
    public Binding searcherDefaultBinding(@Qualifier("directExchange") DirectExchange directExchange) {
        log.info("Searcher binding with configuration: {}", this);
        return BindingBuilder.bind(searcherDefaultQueue()).to(directExchange).withQueueName();
    }

    @Bean
    public Queue searcherDefaultQueue() {
        return QueueBuilder.durable(defaultQueueName)
                .deadLetterExchange(searcherDeadLetterExchange().getName())
                .deadLetterRoutingKey(searcherDeadLetterBinding().getRoutingKey())
                .build();
    }

}
