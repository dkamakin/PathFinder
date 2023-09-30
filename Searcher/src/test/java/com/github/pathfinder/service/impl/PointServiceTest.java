package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.data.Coordinate;
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
    IPointService pointService;

    @BeforeEach
    void setUp() {
        neo4jTestTemplate.cleanDatabase();
    }

    @Test
    void findNearestPoint_PointExists_ReturnNearestPoint() {
        var point      = PointFixtures.point();
        var expected   = pointService.save(point);
        var notNearest = pointService.save(PointFixtures.farPoint(point));
        var coordinate = new Coordinate(expected.getLongitude(), expected.getLatitude());
        var found      = pointService.findNearest(coordinate);

        assertThat(found)
                .get()
                .satisfies(actual -> assertThat(actual.getId())
                        .isNotNull()
                        .isNotEqualTo(notNearest.getId())
                        .isEqualTo(expected.getId()));
    }

}
