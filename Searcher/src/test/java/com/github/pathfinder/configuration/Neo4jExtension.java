package com.github.pathfinder.configuration;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;

public class Neo4jExtension implements BeforeAllCallback, AfterAllCallback {

    @UtilityClass
    public static class Constant {

        public static final String IMAGE_NAME = "neo4j:5.8.0";
    }

    Neo4jContainer<?> neo4j;

    @Override
    public void beforeAll(ExtensionContext context) {
        neo4j = new Neo4jContainer<>(DockerImageName.parse(Constant.IMAGE_NAME));
        neo4j.start();

        System.setProperty("spring.neo4j.uri", neo4j.getBoltUrl());
        System.setProperty("spring.neo4j.authentication.username", "neo4j");
        System.setProperty("spring.neo4j.authentication.password", neo4j.getAdminPassword());
    }

    @Override
    public void afterAll(ExtensionContext context) {

    }
}
