package com.github.pathfinder.security.api.configuration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@ToString
@Configuration
@EqualsAndHashCode
public class SecurityQueueConfiguration {

    public static final class Token {
        public static final String QUEUE_NAME = "${queue.name}";
    }

    @Value(Token.QUEUE_NAME)
    private String queueName;

}
