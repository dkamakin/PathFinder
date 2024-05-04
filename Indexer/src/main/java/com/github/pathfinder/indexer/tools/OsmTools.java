package com.github.pathfinder.indexer.tools;

import com.github.pathfinder.indexer.data.osm.OsmBox;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OsmTools {

    private static final char COMMA = ',';

    public String latitudeLongitude(OsmBox.OsmBoxCoordinate coordinate) {
        return String.valueOf(coordinate.latitude()) + COMMA + coordinate.longitude();
    }

}
