package com.github.pathfinder.indexer.data.index;

public record IndexBox(int id, BoxCoordinate min, BoxCoordinate max) {

    public record BoxCoordinate(Double latitude, Double longitude) {

    }

}
