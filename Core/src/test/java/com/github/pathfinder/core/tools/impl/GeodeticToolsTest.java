package com.github.pathfinder.core.tools.impl;

import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.data.MetersDistance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

class GeodeticToolsTest {

    GeodeticTools target;

    @BeforeEach
    void setUp() {
        target = new GeodeticTools();
    }

    @Test
    void move_DistanceWithAzimuthProvided_MoveAccordingToDistanceAndAzimuth() {
        var startPoint = new Coordinate(45.10356009980752, 19.567944507080036);

        var actual = target.move(startPoint, 270, new MetersDistance(1000));

        assertThat(actual)
                .satisfies(x -> assertThat(x.latitude()).isCloseTo(45.10355939305194, withinPercentage(1)))
                .satisfies(x -> assertThat(x.longitude()).isCloseTo(19.555238781031907, withinPercentage(1)));
    }

    @Test
    void width_BoundingBox_ValidCalculation() {
        var box = new BoundingBox(new Coordinate(45.104546, 19.593430),
                                  new Coordinate(45.314495, 19.963531));

        var actual = target.width(box);

        assertThat(actual.meters()).isCloseTo(29128.150767124087, withinPercentage(1));
    }

    @Test
    void height_BoundingBox_ValidCalculation() {
        var box = new BoundingBox(new Coordinate(45.104546, 19.593430),
                                  new Coordinate(45.314495, 19.963531));

        var actual = target.height(box);

        assertThat(actual.meters()).isCloseTo(23332.865189541073, withinPercentage(1));
    }

}