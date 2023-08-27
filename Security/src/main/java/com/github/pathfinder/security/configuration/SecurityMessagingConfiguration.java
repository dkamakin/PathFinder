package com.github.pathfinder.security.configuration;

import com.github.pathfinder.security.api.messaging.SecurityQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityMessagingConfiguration {

    @Bean
    public Queue securityQueue() {
        return new Queue(SecurityQueue.SECURITY_QUEUE);
    }

}
