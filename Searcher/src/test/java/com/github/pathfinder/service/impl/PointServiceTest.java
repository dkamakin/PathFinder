package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.repository.PointRepository;
import com.github.pathfinder.service.IPointService;
import java.util.Set;
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
    IPointService target;

    void assertEquals(Point point, PointNode actual) {
        assertThat(actual)
                .matches(saved -> saved.getId() != null)
                .matches(saved -> saved.getInternalId() != null)
                .matches(saved -> point.altitude().equals(saved.getAltitude()))
                .matches(saved -> saved.getLandType() == point.landType())
                .matches(saved -> point.latitude().equals(saved.getLatitude()))
                .matches(saved -> point.longitude().equals(saved.getLongitude()));
    }

    @Test
    void save_PointDoesNotExist_SavePoint() {
        var point  = PointFixtures.point();
        var actual = target.save(point);

        assertThat(actual).satisfies(saved -> assertEquals(point, saved));
    }

    @Test
    void save_PointsAreConnected_StoreConnection() {
        var connection  = PointFixtures.pointConnection();
        var sourcePoint = PointFixtures.pointBuilder().connections(Set.of(connection)).build();

        var actual = target.save(sourcePoint);

        assertThat(actual)
                .satisfies(saved -> assertEquals(sourcePoint, saved))
                .satisfies(saved -> assertThat(saved.getRelations())
                        .hasSize(1)
                        .first()
                        .satisfies(relation -> assertEquals(connection.target(), relation.getTarget()))
                        .matches(relation -> connection.distance().equals(relation.getDistance())));

    }

}