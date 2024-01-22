package com.github.pathfinder.database.node;

import com.github.pathfinder.core.data.Coordinate;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.springframework.data.neo4j.types.GeographicPoint2d;

public class ChunkNodeBuilder {

    private GeographicPoint2d min;
    private GeographicPoint2d max;
    private Integer           id;
    private List<PointNode>   points;

    public ChunkNodeBuilder() {
        this.points = Collections.emptyList();
    }

    private ChunkNodeBuilder unpack(Coordinate coordinate, BiFunction<Double, Double, ChunkNodeBuilder> target) {
        return target.apply(coordinate.latitude(), coordinate.longitude());
    }

    public ChunkNodeBuilder max(Coordinate coordinate) {
        return unpack(coordinate, this::max);
    }

    public ChunkNodeBuilder min(Coordinate coordinate) {
        return unpack(coordinate, this::min);
    }

    public ChunkNodeBuilder max(double latitude, double longitude) {
        return setAndReturnThis(max, x -> this.max = new GeographicPoint2d(latitude, longitude));
    }

    public ChunkNodeBuilder min(double latitude, double longitude) {
        return setAndReturnThis(min, x -> this.min = new GeographicPoint2d(latitude, longitude));
    }

    public ChunkNodeBuilder id(Integer id) {
        return setAndReturnThis(id, x -> this.id = x);
    }

    public ChunkNodeBuilder points(List<PointNode> relations) {
        return setAndReturnThis(relations, x -> this.points = x);
    }

    public ChunkNode build() {
        return new ChunkNode(id, min, max, points);
    }

    private <T> ChunkNodeBuilder setAndReturnThis(T value, Consumer<T> consumer) {
        consumer.accept(value);
        return this;
    }

}
