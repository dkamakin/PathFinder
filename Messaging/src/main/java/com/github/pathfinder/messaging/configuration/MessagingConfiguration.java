package com.github.pathfinder.messaging.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pathfinder.messaging.converter.CoreMessageConverter;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@ComponentScan("com.github.pathfinder.messaging")
public class MessagingConfiguration {

    @Bean
    public MessageConverter messageConverter(Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        return new CoreMessageConverter(jackson2JsonMessageConverter);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public DirectExchange directExchange(AmqpConfiguration configuration) {
        return new DirectExchange(configuration.getDirectExchangeName());
    }

}
