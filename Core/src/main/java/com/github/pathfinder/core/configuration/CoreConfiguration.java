package com.github.pathfinder.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.github.pathfinder.core", includeFilters = @ComponentScan.Filter(Aspect.class))
public class CoreConfiguration {

    public static ObjectMapper objectMapperFactory() {
        var mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return objectMapperFactory();
    }

}
