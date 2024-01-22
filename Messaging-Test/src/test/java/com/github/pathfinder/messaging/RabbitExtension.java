package com.github.pathfinder.messaging;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.RabbitMQContainer;

public class RabbitExtension implements BeforeAllCallback {

    private static final String RABBIT_IMAGE = "rabbitmq:3.12.12-management-alpine";

    private final RabbitMQContainer rabbitMQContainer;

    public RabbitExtension() {
        this.rabbitMQContainer = new RabbitMQContainer(RABBIT_IMAGE);
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        rabbitMQContainer.start();

        SpringRabbitProperties.setHost(rabbitMQContainer.getHost());
        SpringRabbitProperties.setPort(rabbitMQContainer.getAmqpPort().toString());
        SpringRabbitProperties.setPassword(rabbitMQContainer.getAdminPassword());
        SpringRabbitProperties.setUsername(rabbitMQContainer.getAdminUsername());
    }

}

