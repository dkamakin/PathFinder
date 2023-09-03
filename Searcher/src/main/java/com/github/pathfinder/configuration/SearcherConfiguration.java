package com.github.pathfinder.configuration;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.security.api.configuration.SecurityApiConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@Import({CoreConfiguration.class, SecurityApiConfiguration.class})
@EnableNeo4jRepositories(basePackages = "com.github.pathfinder.database", transactionManagerRef = "neo4jTransactionManager")
public class SearcherConfiguration {

}
