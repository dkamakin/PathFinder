package com.github.pathfinder.indexer.data.osm;

public record OsmBox(OsmBoxCoordinate min, OsmBoxCoordinate max) {

    public record OsmBoxCoordinate(double latitude, double longitude) {
    }
}
