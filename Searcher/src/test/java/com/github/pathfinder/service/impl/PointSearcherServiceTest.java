package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.searcher.api.exception.PointNotFoundException;
import com.github.pathfinder.service.IPointSearcherService;
import com.github.pathfinder.service.IPointService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SearcherNeo4jTest
@Import({PointSearcherService.class, PointService.class, ProjectionService.class})
class PointSearcherServiceTest {

    @Autowired
    IPointSearcherService target;

    @Autowired
    IPointService pointService;

    @Test
    void findNearest_PointDoesNotExist_PointNotFoundException() {
        var coordinate = PointFixtures.COORDINATE;

        assertThatThrownBy(() -> target.findNearest(coordinate)).isInstanceOf(PointNotFoundException.class);
    }

    @Test
    void findNearest_PointIsTooFarAway_PointNotFoundException() {
        var point      = PointFixtures.randomPointNode();
        var coordinate = new Coordinate(point.latitude(), point.longitude() + 1);

        assertThatThrownBy(() -> target.findNearest(coordinate)).isInstanceOf(PointNotFoundException.class);
    }

    @Test
    void findNearest_PointIsNotTooFar_ReturnPoint() {
        var nearestPoint = PointFixtures.randomPointNodeBuilder()
                .location(44.827410791880155, 20.419468330585666, 1D).build();
        var noisePoint = PointFixtures.randomPointNodeBuilder()
                .location(44.82744118518296, 20.419457053285115, 1D).build();
        var notNearest = PointFixtures.randomPointNodeBuilder()
                .location(44.82755949185502, 20.413331663727266, 1D).build();
        var coordinate = new Coordinate(44.827452775846965, 20.419722423975298);

        pointService.saveAll(List.of(nearestPoint, noisePoint, notNearest));

        var found = target.findNearest(coordinate);

        assertThat(found)
                .satisfies(actual -> assertThat(actual.getId())
                        .isNotEqualTo(noisePoint.getId())
                        .isNotEqualTo(notNearest.getId())
                        .isEqualTo(nearestPoint.getId()));
    }

}
