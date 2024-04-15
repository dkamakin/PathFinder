package com.github.pathfinder.searcher.api.configuration;

import com.github.pathfinder.messaging.configuration.MessagingConfiguration;
import com.github.pathfinder.messaging.configuration.RabbitFactories;
import com.github.pathfinder.messaging.converter.CoreMessageConverter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Getter
@ToString
@Validated
@Configuration
@EqualsAndHashCode
@Import(MessagingConfiguration.class)
public class SearcherMessagingConfiguration {

    @UtilityClass
    public static class Token {

        public static final String DEFAULT_QUEUE_NAME                      = "${queue.searcher.default.name}";
        public static final String DEAD_LETTER_QUEUE_NAME                  = "${queue.searcher.deadLetter.name}";
        public static final String CONNECTIONS_QUEUE_NAME                  = "${queue.searcher.connections.name}";
        public static final String CONNECTIONS_CONSUMERS                   = "${queue.searcher.connections.consumers:2}";
        public static final String SAVE_CHUNKS_QUEUE_NAME                  = "${queue.searcher.save-chunks.name}";
        public static final String SAVE_CHUNKS_CONSUMERS                   = "${queue.searcher.save-chunks.consumers:15}";
        public static final String CONNECTIONS_LISTENER_QUEUE_FACTORY_NAME = "connectionsListenerFactory";
        public static final String SAVE_CHUNKS_LISTENER_QUEUE_FACTORY_NAME = "saveChunksListenerFactory";

    }

    @NotBlank
    @Value(Token.DEFAULT_QUEUE_NAME)
    private String defaultQueueName;

    @NotBlank
    @Value(Token.DEAD_LETTER_QUEUE_NAME)
    private String deadLetterQueueName;

    @NotBlank
    @Value(Token.CONNECTIONS_QUEUE_NAME)
    private String connectionsQueueName;

    @NotBlank
    @Value(Token.SAVE_CHUNKS_QUEUE_NAME)
    private String saveChunksQueueName;

    @NotBlank
    @Value("${exchange.searcher.deadLetter.name}")
    private String deadLetterExchangeName;

    @NotNull
    @Positive
    @Value(Token.CONNECTIONS_CONSUMERS)
    private Integer connectionsConsumers;

    @NotNull
    @Positive
    @Value(Token.SAVE_CHUNKS_CONSUMERS)
    private Integer saveChunksConsumers;

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
    public Binding searcherSaveChunksBinding(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(searcherSaveChunksQueue()).to(directExchange).withQueueName();
    }

    @Bean
    public Queue searcherSaveChunksQueue() {
        return QueueBuilder.durable(saveChunksQueueName)
                .deadLetterExchange(searcherDeadLetterExchange().getName())
                .deadLetterRoutingKey(searcherDeadLetterBinding().getRoutingKey())
                .build();
    }

    @Bean
    public Binding searcherConnectionsBinding(@Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(searcherConnectionsQueue()).to(directExchange).withQueueName();
    }

    @Bean
    public Queue searcherConnectionsQueue() {
        return QueueBuilder.durable(connectionsQueueName)
                .deadLetterExchange(searcherDeadLetterExchange().getName())
                .deadLetterRoutingKey(searcherDeadLetterBinding().getRoutingKey())
                .build();
    }

    @Bean
    public Binding searcherDefaultBinding(@Qualifier("directExchange") DirectExchange directExchange) {
        log.info("Searcher binding with configuration: {}", this);
        return BindingBuilder.bind(searcherDefaultQueue()).to(directExchange).withQueueName();
    }

    @Bean
    public Queue searcherDefaultQueue() {
        return QueueBuilder.durable(defaultQueueName).build();
    }

    @Bean(Token.CONNECTIONS_LISTENER_QUEUE_FACTORY_NAME)
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> connectionsQueueListenerFactory(
            ConnectionFactory connectionFactory, CoreMessageConverter messageConverter) {
        log.info("Building a connections listener factory with configuration: {}", this);

        return RabbitFactories.listenerFactory(connectionFactory, messageConverter, connectionsConsumers);
    }

    @Bean(Token.SAVE_CHUNKS_LISTENER_QUEUE_FACTORY_NAME)
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> saveChunksQueueListenerFactory(
            ConnectionFactory connectionFactory, CoreMessageConverter messageConverter) {
        return RabbitFactories.listenerFactory(connectionFactory, messageConverter, saveChunksConsumers);
    }

}
