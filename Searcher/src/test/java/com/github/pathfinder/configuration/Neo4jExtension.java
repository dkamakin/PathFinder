package com.github.pathfinder.configuration;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.Neo4jLabsPlugin;
import org.testcontainers.utility.DockerImageName;

public class Neo4jExtension implements BeforeAllCallback, BeforeEachCallback {

    @UtilityClass
    public static class Constant {

        public static final String IMAGE_NAME = "neo4j:5.15.0-community-ubi8";
    }

    Neo4jContainer<?> neo4j;

    @Override
    public void beforeAll(ExtensionContext context) {
        neo4j = new Neo4jContainer<>(DockerImageName.parse(Constant.IMAGE_NAME)).withLabsPlugins(Neo4jLabsPlugin.APOC);

        neo4j.start();

        setProperties(neo4j);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        ApplicationContext springContext = SpringExtension.getApplicationContext(context);
        springContext.getBean(Neo4jTestTemplate.class).cleanDatabase();
    }

    void setProperties(Neo4jContainer<?> neo4j) {
        System.setProperty("spring.neo4j.uri", neo4j.getBoltUrl());
        System.setProperty("spring.neo4j.authentication.username", "neo4j");
        System.setProperty("spring.neo4j.authentication.password", neo4j.getAdminPassword());
    }

}
