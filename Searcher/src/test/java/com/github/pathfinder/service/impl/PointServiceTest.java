package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.point.IndexedPoint;
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
    void save_NewPointProvided_SavedSuccessfully() {
        var actual = pointService.save(PointFixtures.point());

        assertThat(actual)
                .extracting(IndexedPoint::id)
                .isNotNull();
    }

    @Test
    void findNearestPoint_PointExists_ReturnNearestPoint() {
        var point      = PointFixtures.point();
        var actual     = pointService.save(point);
        var notNearest = pointService.save(PointFixtures.farPoint(point));
        var coordinate = new Coordinate(actual.point().longitude(), actual.point().latitude());
        var found      = pointService.findNearest(coordinate);

        assertThat(found)
                .get()
                .extracting(IndexedPoint::id)
                .isEqualTo(actual.id())
                .isNotNull();
    }

}
