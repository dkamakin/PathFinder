package com.github.pathfinder;

import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.PointNodeBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PointFixtures {

    public static final String     LAND_TYPE  = "DUNE";
    public static final double     LATITUDE   = 20.457924760622614;
    public static final double     LONGITUDE  = 44.81660396625469;
    public static final Coordinate COORDINATE = new Coordinate(LATITUDE, LONGITUDE);

    public static PointNode randomPointNode() {
        return randomPointNodeBuilder().build();
    }

    public static PointNodeBuilder randomPointNodeBuilder() {
        return PointNode.builder()
                .location(Math.random(), Math.random(), Math.random())
                .passabilityCoefficient(Math.random())
                .landType(LAND_TYPE);
    }

}
