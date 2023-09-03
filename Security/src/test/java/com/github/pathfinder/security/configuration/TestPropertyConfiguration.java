package com.github.pathfinder.security.configuration;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;

@TestConfiguration
public class TestPropertyConfiguration {

    @Bean
    public ConversionService conversionService() {
        return new ApplicationConversionService();
    }

}
