package com.github.pathfinder.indexer.data.elevation;

import java.util.List;

public record OpenElevationResponse(List<OpenElevationPoint> results) {

    public record OpenElevationPoint(Double longitude, Double latitude, Double elevation) {

    }

}
