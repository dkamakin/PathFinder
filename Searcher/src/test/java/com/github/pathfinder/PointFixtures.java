package com.github.pathfinder;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.entity.LandType;
import com.github.pathfinder.database.entity.PointEntity;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PointFixtures {

    public static final double     LATITUDE   = 20.457924760622614;
    public static final double     LONGITUDE  = 44.81660396625469;
    public static final Coordinate COORDINATE = new Coordinate(LONGITUDE, LATITUDE);

    public static PointEntity pointEntity() {
        return new PointEntity(randomDouble(), randomDouble(), randomDouble(), LandType.DUNE);
    }

    public static Point.PointConnection pointConnection() {
        return new Point.PointConnection(PointFixtures.point(), randomDouble());
    }

    public static Point point() {
        return pointBuilder().build();
    }

    public static Point pointWithConnection() {
        return pointBuilder().connections(Set.of(pointConnection())).build();
    }

    public static Point.PointBuilder pointBuilder() {
        return Point.builder().altitude(randomDouble()).longitude(randomDouble()).latitude(randomDouble())
                .landType(LandType.DUNE);
    }

    public static Point farPoint(Point point) {
        return new Point(point.altitude() + randomDouble(),
                         point.longitude() + randomDouble(),
                         point.latitude(),
                         point.landType());
    }

    private static Double randomDouble() {
        return Math.random();
    }

}
