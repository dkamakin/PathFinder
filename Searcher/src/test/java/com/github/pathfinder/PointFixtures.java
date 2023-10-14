package com.github.pathfinder;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.entity.LandType;
import com.github.pathfinder.database.entity.PointEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PointFixtures {

    public static final double     LATITUDE   = 20.457924760622614;
    public static final double     LONGITUDE  = 44.81660396625469;
    public static final Coordinate COORDINATE = new Coordinate(LONGITUDE, LATITUDE);

    public static PointEntity pointEntity() {
        return new PointEntity(1D, LONGITUDE, LATITUDE, LandType.DUNE);
    }

    public static Point point() {
        return pointBuilder().build();
    }

    public static Point.PointBuilder pointBuilder() {
        return Point.builder().altitude(1D).longitude(LONGITUDE).latitude(LATITUDE).landType(LandType.DUNE);
    }

    public static Point farPoint(Point point) {
        return new Point(point.altitude() * 2 + Math.random(), point.longitude() + Math.random(), point.latitude(),
                         point.landType());
    }

}
