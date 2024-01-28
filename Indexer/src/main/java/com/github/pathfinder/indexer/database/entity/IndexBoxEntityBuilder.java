package com.github.pathfinder.indexer.database.entity;

import java.time.Instant;
import java.util.function.Consumer;

public class IndexBoxEntityBuilder {

    private boolean          connected;
    private boolean          saved;
    private Instant          saveRequestTimestamp;
    private Instant          connectionRequestTimestamp;
    private MaxBoxCoordinate max;
    private MinBoxCoordinate min;

    public IndexBoxEntityBuilder connectionRequestTimestamp(Instant connectionRequestTimestamp) {
        return setAndReturnThis(connectionRequestTimestamp, x -> this.connectionRequestTimestamp = x);
    }

    public IndexBoxEntityBuilder saveRequestTimestamp(Instant saveRequestTimestamp) {
        return setAndReturnThis(saveRequestTimestamp, x -> this.saveRequestTimestamp = x);
    }

    public IndexBoxEntityBuilder saved(boolean saved) {
        return setAndReturnThis(saved, x -> this.saved = x);
    }

    public IndexBoxEntityBuilder connected(boolean connected) {
        return setAndReturnThis(connected, x -> this.connected = x);
    }

    public IndexBoxEntityBuilder min(double latitude, double longitude) {
        return setAndReturnThis(new MinBoxCoordinate(latitude, longitude), x -> this.min = x);
    }

    public IndexBoxEntityBuilder max(double latitude, double longitude) {
        return setAndReturnThis(new MaxBoxCoordinate(latitude, longitude), x -> this.max = x);
    }

    public IndexBoxEntity build() {
        return new IndexBoxEntity(null, saved, connected, saveRequestTimestamp, connectionRequestTimestamp, min, max);
    }

    private <T> IndexBoxEntityBuilder setAndReturnThis(T value, Consumer<T> consumer) {
        consumer.accept(value);
        return this;
    }

}
