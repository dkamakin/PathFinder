package com.github.pathfinder.messaging.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pathfinder.messaging.converter.CoreMessageConverter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SmartMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Slf4j
@Data
@Validated
@EnableRabbit
@RefreshScope
@Configuration
@ComponentScan("com.github.pathfinder.messaging")
public class MessagingConfiguration implements RabbitListenerConfigurer {

    @NotBlank
    @ToString.Exclude
    @Value("${spring.rabbitmq.password}")
    private String password;

    @NotBlank
    @Value("${spring.rabbitmq.username}")
    private String username;

    @NotNull
    @Positive
    @Value("${spring.rabbitmq.port:5672}")
    private Integer port;

    @NotBlank
    @Value("${spring.rabbitmq.host}")
    private String host;

    @NotNull
    @Positive
    @Value("${rabbit.max.consumers:100}")
    private Integer concurrentConsumers;

    @NotBlank
    @Value("${exchange.direct.name}")
    private String directExchangeName;

    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setValidator(localValidatorFactoryBean);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, CoreMessageConverter messageConverter) {
        var factory = new SimpleRabbitListenerContainerFactory();

        log.info("Building a listener factory with configuration: {}", this);

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setMaxConcurrentConsumers(concurrentConsumers);
        factory.setDefaultRequeueRejected(false);

        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, CoreMessageConverter messageConverter) {
        var template = new RabbitTemplate(connectionFactory);

        template.setMessageConverter(messageConverter);

        return template;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        var factory = new CachingConnectionFactory(host, port);

        factory.setUsername(username);
        factory.setPassword(password);

        return factory;
    }

    @Bean
    public SmartMessageConverter messageConverter(Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        return new CoreMessageConverter(jackson2JsonMessageConverter);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(directExchangeName).build();
    }

}
