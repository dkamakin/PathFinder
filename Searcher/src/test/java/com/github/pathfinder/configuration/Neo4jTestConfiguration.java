package com.github.pathfinder.configuration;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.database.mapper.ValueMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@TestConfiguration
@EnableNeo4jRepositories
@Import({Neo4jConfiguration.class, Neo4jTestTemplate.class, ValueMapper.class, CoreConfiguration.class})
public class Neo4jTestConfiguration {

}