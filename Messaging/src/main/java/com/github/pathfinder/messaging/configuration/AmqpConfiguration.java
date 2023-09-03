package com.github.pathfinder.messaging.configuration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@ToString
@Configuration
@EqualsAndHashCode
public class AmqpConfiguration {

    public static final class Token {

        public static final String EXCHANGE_NAME = "${direct-exchange.name}";
    }

    @Value(Token.EXCHANGE_NAME)
    private String directExchangeName;

}
