package com.github.pathfinder.messaging.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pathfinder.messaging.converter.CoreMessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SmartMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
@ComponentScan("com.github.pathfinder.messaging")
public class MessagingConfiguration implements RabbitListenerConfigurer {

    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setValidator(localValidatorFactoryBean);
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
    public DirectExchange directExchange(AmqpConfiguration configuration) {
        return new DirectExchange(configuration.getDirectExchangeName());
    }

}
