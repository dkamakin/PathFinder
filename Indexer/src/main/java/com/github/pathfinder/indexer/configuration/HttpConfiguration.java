package com.github.pathfinder.indexer.configuration;

import java.net.http.HttpClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Slf4j
@Validated
@RefreshScope
@Configuration
public class HttpConfiguration {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

}
