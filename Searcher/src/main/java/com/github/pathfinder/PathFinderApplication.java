package com.github.pathfinder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class PathFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PathFinderApplication.class, args);
    }

}
