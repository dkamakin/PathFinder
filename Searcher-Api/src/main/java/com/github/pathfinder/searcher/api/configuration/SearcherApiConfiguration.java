package com.github.pathfinder.searcher.api.configuration;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
@ComponentScan("com.github.pathfinder.searcher.api")
public class SearcherApiConfiguration {

}
