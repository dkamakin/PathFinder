package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.distance.IDistance;
import com.github.pathfinder.data.distance.MetersDistance;
import com.github.pathfinder.database.repository.PointRepository;
import com.github.pathfinder.exception.PointNotFoundException;
import com.github.pathfinder.service.IDistanceCalculator;
import com.github.pathfinder.service.IPointSearcherService;
import com.github.pathfinder.service.IPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SearcherNeo4jTest
@Import({PointSearcherService.class, PointService.class})
class PointSearcherServiceTest {

    @MockBean
    IDistanceCalculator distanceCalculator;

    @MockBean
    CoordinateConfiguration coordinateConfiguration;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    Neo4jTestTemplate neo4jTestTemplate;

    @Autowired
    IPointSearcherService target;

    @Autowired
    IPointService pointService;

    @BeforeEach
    void setUp() {
        neo4jTestTemplate.cleanDatabase();
    }

    void whenNeedToGetAccuracy(Double accuracyMeters) {
        when(coordinateConfiguration.getDistanceAccuracyMeters()).thenReturn(accuracyMeters);
    }

    void whenNeedToCalculateDistance(Coordinate source, Coordinate target, IDistance expected) {
        when(distanceCalculator.distance(source, target)).thenReturn(expected);
    }

    @Test
    void findNearest_PointDoesNotExist_PointNotFoundException() {
        var coordinate = PointFixtures.COORDINATE;

        assertThatThrownBy(() -> target.findNearest(coordinate)).isInstanceOf(PointNotFoundException.class);
    }

    @Test
    void findNearest_PointIsTooFarAway_PointNotFoundException() {
        var accuracyMeters = 45D;
        var distance       = new MetersDistance(accuracyMeters + 0.1D);
        var point          = pointService.save(PointFixtures.point());
        var coordinate     = new Coordinate(point.getLongitude(), point.getLatitude());

        whenNeedToGetAccuracy(accuracyMeters);
        whenNeedToCalculateDistance(coordinate, coordinate, distance);

        assertThatThrownBy(() -> target.findNearest(coordinate)).isInstanceOf(PointNotFoundException.class);
    }

    @Test
    void findNearest_PointIsNotTooFar_ReturnPoint() {
        var firstPoint     = pointService.save(PointFixtures.point());
        var expected       = pointService.save(PointFixtures.point());
        var notNearest     = pointService.save(PointFixtures.point());
        var coordinate     = new Coordinate(expected.getLongitude(), expected.getLatitude());
        var accuracyMeters = 45D;
        var distance       = new MetersDistance(accuracyMeters - 1D);

        whenNeedToGetAccuracy(accuracyMeters);
        whenNeedToCalculateDistance(coordinate, coordinate, distance);

        var found = target.findNearest(coordinate);

        assertThat(found)
                .satisfies(actual -> assertThat(actual.getId())
                        .isNotNull()
                        .isNotEqualTo(firstPoint.getId())
                        .isNotEqualTo(notNearest.getId())
                        .isEqualTo(expected.getId()));
    }

}
