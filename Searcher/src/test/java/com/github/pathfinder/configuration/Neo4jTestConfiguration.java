package com.github.pathfinder.configuration;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.core.Neo4jTemplate;

@TestConfiguration
@Import(CoreConfiguration.class)
public class Neo4jTestConfiguration {

    @Bean
    public Neo4jTestTemplate neo4jTestContext(Neo4jTemplate neo4jTemplate) {
        return new Neo4jTestTemplate(neo4jTemplate);
    }

}