package com.github.pathfinder.core.tools.impl;

import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.data.Distance;
import com.github.pathfinder.core.data.MetersDistance;
import org.geotools.referencing.GeodeticCalculator;

public class GeodeticTools {

    private final GeodeticCalculator calculator;

    public GeodeticTools() {
        this.calculator = new GeodeticCalculator();
    }

    public Coordinate move(Coordinate coordinate, double azimuth, Distance distance) {
        calculator.setStartingGeographicPoint(coordinate.longitude(), coordinate.latitude());
        calculator.setDirection(azimuth, distance.meters());

        var destinationPoint = calculator.getDestinationGeographicPoint();

        return new Coordinate(destinationPoint.getY(), destinationPoint.getX());
    }

    public Distance width(BoundingBox box) {
        calculator.setStartingGeographicPoint(box.min().longitude(), box.min().latitude());
        calculator.setDestinationGeographicPoint(box.max().longitude(), box.min().latitude());
        return new MetersDistance(calculator.getOrthodromicDistance());
    }

    public Distance height(BoundingBox box) {
        calculator.setStartingGeographicPoint(box.min().longitude(), box.min().latitude());
        calculator.setDestinationGeographicPoint(box.min().longitude(), box.max().latitude());
        return new MetersDistance(calculator.getOrthodromicDistance());
    }

}
