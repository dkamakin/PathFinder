package com.github.pathfinder;

import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.entity.LandType;

public class PointFixtures {

    public static Point point() {
        return new Point(1, 1, 1, LandType.BAY);
    }

    public static Point farPoint(Point point) {
        return new Point(point.altitude() * 2, point.longitude() * 3, point.latitude(),
                         point.landType());
    }

}
