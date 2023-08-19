package com.github.pathfinder.configuration;

import com.github.pathfinder.database.entity.PointEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.neo4j.core.Neo4jTemplate;

@TestComponent
@RequiredArgsConstructor
public class Neo4jTestTemplate {

    private final Neo4jTemplate neo4jTemplate;

    public void cleanDatabase() {
        neo4jTemplate.deleteAll(PointEntity.class);
    }

}
