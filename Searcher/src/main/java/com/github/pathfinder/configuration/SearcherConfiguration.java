package com.github.pathfinder.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.github.pathfinder")
@EnableNeo4jRepositories(basePackages = "com.github.pathfinder.database", transactionManagerRef = "neo4jTransactionManager")
public class SearcherConfiguration {

}
