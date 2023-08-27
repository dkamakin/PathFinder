package com.github.pathfinder.security.api.configuration;

import com.github.pathfinder.messaging.configuration.MessagingConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MessagingConfiguration.class)
public class SecurityApiConfiguration {
}
