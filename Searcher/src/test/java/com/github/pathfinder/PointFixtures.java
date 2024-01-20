package com.github.pathfinder;

import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.ChunkNodeBuilder;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.PointNodeBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PointFixtures {

    public static final String LAND_TYPE = "DUNE";
    public static final double LATITUDE  = 20.457924760622614;
    public static final double LONGITUDE = 44.81660396625469;

    public static ChunkNodeBuilder randomChunkNodeBuilder() {
        return ChunkNode.builder()
                .max(PointFixtures.randomCoordinate())
                .min(PointFixtures.randomCoordinate());
    }

    public static PointNode randomPointNode() {
        return randomPointNodeBuilder().build();
    }

    public static PointNodeBuilder randomPointNodeBuilder() {
        return pointNodeBuilder()
                .location(Math.random(), Math.random(), Math.random())
                .passabilityCoefficient(Math.random());
    }

    public static PointNodeBuilder pointNodeBuilder() {
        return PointNode.builder()
                .landType(LAND_TYPE);
    }

    public static Coordinate randomCoordinate() {
        return new Coordinate(Math.random(), Math.random());
    }

}
