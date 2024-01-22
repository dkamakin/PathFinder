package com.github.pathfinder.indexer.client.elevation;

import com.github.pathfinder.indexer.data.elevation.Elevation;
import com.github.pathfinder.indexer.data.elevation.ElevationCoordinate;
import java.util.List;

public interface ElevationClient {

    List<Elevation> elevations(List<ElevationCoordinate> coordinates);

}
