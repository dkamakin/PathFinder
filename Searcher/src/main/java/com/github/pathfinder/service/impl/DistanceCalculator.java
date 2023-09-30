package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.log.LogLevel;
import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.distance.IDistance;
import com.github.pathfinder.data.distance.MetersDistance;
import com.github.pathfinder.service.IDistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.referencing.GeodeticCalculator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistanceCalculator implements IDistanceCalculator {

    @Override
    @Logged(value = {"source", "target"}, logLevel = LogLevel.DEBUG)
    public IDistance distance(Coordinate source, Coordinate target) {
        var calculator = new GeodeticCalculator();

        calculator.setStartingGeographicPoint(source.longitude(), source.latitude());
        calculator.setDestinationGeographicPoint(target.longitude(), target.latitude());

        return new MetersDistance(calculator.getOrthodromicDistance());
    }

}
