package com.github.pathfinder.configuration;

import com.github.pathfinder.database.entity.PointEntity;
import com.github.pathfinder.database.entity.PointRelation;
import com.github.pathfinder.database.repository.ProjectionRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@RequiredArgsConstructor
public class Neo4jTestTemplate {

    private final Neo4jTemplate        neo4jTemplate;
    private final TestNeo4jRepository  testRepository;
    private final ProjectionRepository projectionRepository;

    @Transactional
    public void cleanDatabase() {
        Set.of(PointEntity.class, PointRelation.class).forEach(neo4jTemplate::deleteAll);
        testRepository.graphNames().forEach(projectionRepository::tryDelete);
    }

}
