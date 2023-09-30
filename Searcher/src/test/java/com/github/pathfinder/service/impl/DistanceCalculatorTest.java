package com.github.pathfinder.service.impl;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.distance.IDistance;
import com.github.pathfinder.data.distance.MetersDistance;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DistanceCalculatorTest {

    @InjectMocks
    DistanceCalculator distanceCalculator;

    static Stream<Arguments> distances() {
        return Stream.of(
                Arguments.of(new Coordinate(44.82056430144023, 20.415257306040886),
                             new Coordinate(44.81660396625469, 20.457924760622614),
                             new MetersDistance(4741.74965621422)),
                Arguments.of(new Coordinate(44.82056430144023, 20.415257306040886),
                             new Coordinate(59.84206061713256, 30.251511114556415),
                             new MetersDistance(1860804.8366489974)),
                Arguments.of(new Coordinate(44.82056430144023, 20.415257306040886),
                             new Coordinate(47.55594860444473, 19.07580669241495),
                             new MetersDistance(322770.2404046728))
        );
    }

    @ParameterizedTest
    @MethodSource("distances")
    void distance_DifferentDistances_ValidCalculations(Coordinate source, Coordinate target, IDistance expected) {
        var actual = distanceCalculator.distance(source, target);

        assertThat(actual).isEqualTo(expected);
    }

}
