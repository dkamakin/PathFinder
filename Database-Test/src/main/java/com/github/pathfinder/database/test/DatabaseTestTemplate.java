package com.github.pathfinder.database.test;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class DatabaseTestTemplate {

    private static final String POSTGRES_IMAGE = "postgres:16.2";

    private final PostgreSQLContainer<?> container;

    public DatabaseTestTemplate() {
        container = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE));
    }

    public void initialize() {
        container.start();

        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
    }

}
