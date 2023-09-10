package com.github.pathfinder.core.web.configuration.impl;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreWebConfiguration {

    private static final String SECURITY_SCHEME      = "bearer";
    private static final String BEARER_FORMAT        = "JWT";
    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                                    .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                                        new SecurityScheme()
                                                                .name(SECURITY_SCHEME_NAME)
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme(SECURITY_SCHEME)
                                                                .bearerFormat(BEARER_FORMAT)));
    }
}
