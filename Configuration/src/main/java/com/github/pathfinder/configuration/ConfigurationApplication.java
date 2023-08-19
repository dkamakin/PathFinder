package com.github.pathfinder.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@Slf4j
@EnableConfigServer
@SpringBootApplication
public class ConfigurationApplication {

    public static void main(String[] args) {
        log.info("Loading from: {}", System.getenv("GIT_URI"));
        SpringApplication.run(ConfigurationApplication.class, args);
    }

}
