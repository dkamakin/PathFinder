package com.github.pathfinder.messaging;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.RabbitMQContainer;

public class RabbitExtension implements BeforeAllCallback, AfterAllCallback {

    private static final String RABBIT_IMAGE = "rabbitmq:3.12-management-alpine";

    RabbitMQContainer rabbitMQContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        rabbitMQContainer = new RabbitMQContainer(RABBIT_IMAGE);
        rabbitMQContainer.start();

        System.setProperty("spring.rabbitmq.host", rabbitMQContainer.getHost());
        System.setProperty("spring.rabbitmq.port", rabbitMQContainer.getAmqpPort().toString());
    }

    @Override
    public void afterAll(ExtensionContext context) {

    }
}

