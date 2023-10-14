package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.database.repository.PointRepository;
import com.github.pathfinder.service.IPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@SearcherNeo4jTest
@Import(PointService.class)
class PointServiceTest {

    @Autowired
    PointRepository pointRepository;

    @Autowired
    Neo4jTestTemplate neo4jTestTemplate;

    @Autowired
    IPointService target;

    @BeforeEach
    void setUp() {
        neo4jTestTemplate.cleanDatabase();
    }

    @Test
    void save_PointDoesNotExist_SavePoint() {
        var point  = PointFixtures.point();
        var actual = target.save(point);

        assertThat(actual)
                .matches(saved -> saved.getId() != null)
                .matches(saved -> point.altitude().equals(saved.getAltitude()))
                .matches(saved -> saved.getLandType() == point.landType())
                .matches(saved -> point.latitude().equals(saved.getLatitude()))
                .matches(saved -> point.longitude().equals(saved.getLongitude()));
    }

}