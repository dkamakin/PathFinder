package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.distance.IDistance;
import com.github.pathfinder.data.distance.MetersDistance;
import com.github.pathfinder.database.entity.PointEntity;
import com.github.pathfinder.exception.PointNotFoundException;
import com.github.pathfinder.service.IDistanceCalculator;
import com.github.pathfinder.service.IPointService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NearestPointSearcherTest {

    @InjectMocks
    NearestPointSearcher target;

    @Mock
    IDistanceCalculator distanceCalculator;

    @Mock
    CoordinateConfiguration coordinateConfiguration;

    @Mock
    IPointService pointService;

    void whenNeedToGetAccuracy(Double accuracyMeters) {
        when(coordinateConfiguration.getDistanceAccuracyMeters()).thenReturn(accuracyMeters);
    }

    void whenNeedToCalculateDistance(Coordinate source, Coordinate target, IDistance expected) {
        when(distanceCalculator.distance(source, target)).thenReturn(expected);
    }

    void whenNeedToGetPoint(Coordinate coordinate, PointEntity expected) {
        when(pointService.findNearest(coordinate)).thenReturn(Optional.ofNullable(expected));
    }

    @Test
    void findNearest_PointDoesNotExist_PointNotFoundException() {
        var coordinate = PointFixtures.COORDINATE;

        whenNeedToGetPoint(PointFixtures.COORDINATE, null);

        assertThatThrownBy(() -> target.findNearest(coordinate)).isInstanceOf(PointNotFoundException.class);

        verifyNoInteractions(coordinateConfiguration);
        verifyNoInteractions(distanceCalculator);
    }

    @Test
    void findNearest_PointIsTooFat_PointNotFoundException() {
        var coordinate     = PointFixtures.COORDINATE;
        var point          = PointFixtures.pointEntity();
        var accuracyMeters = 45D;
        var distance       = new MetersDistance(accuracyMeters + 0.1D);

        whenNeedToGetPoint(PointFixtures.COORDINATE, point);
        whenNeedToGetAccuracy(accuracyMeters);
        whenNeedToCalculateDistance(coordinate, new Coordinate(point.getLongitude(), point.getLatitude()), distance);

        assertThatThrownBy(() -> target.findNearest(coordinate)).isInstanceOf(PointNotFoundException.class);
    }

    @Test
    void findNearest_PointIsFound_ReturnPoint() {
        var coordinate     = PointFixtures.COORDINATE;
        var point          = PointFixtures.pointEntity();
        var accuracyMeters = 45D;
        var distance       = new MetersDistance(accuracyMeters - 0.1D);

        whenNeedToGetPoint(PointFixtures.COORDINATE, point);
        whenNeedToGetAccuracy(accuracyMeters);
        whenNeedToCalculateDistance(coordinate, new Coordinate(point.getLongitude(), point.getLatitude()), distance);

        var actual = target.findNearest(coordinate);

        assertThat(actual).isEqualTo(point);
    }

}
