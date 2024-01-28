package com.github.pathfinder.indexer.database.entity;

import java.util.function.Consumer;

public class RegionEntityBuilder {

    private boolean          processed;
    private MaxBoxCoordinate max;
    private MinBoxCoordinate min;

    public RegionEntityBuilder processed(boolean processed) {
        return setAndReturnThis(processed, x -> this.processed = x);
    }

    public RegionEntityBuilder min(double latitude, double longitude) {
        return setAndReturnThis(new MinBoxCoordinate(latitude, longitude), x -> this.min = x);
    }

    public RegionEntityBuilder max(double latitude, double longitude) {
        return setAndReturnThis(new MaxBoxCoordinate(latitude, longitude), x -> this.max = x);
    }

    public RegionEntity build() {
        return new RegionEntity(null, processed, min, max);
    }

    private <T> RegionEntityBuilder setAndReturnThis(T value, Consumer<T> consumer) {
        consumer.accept(value);
        return this;
    }

}
