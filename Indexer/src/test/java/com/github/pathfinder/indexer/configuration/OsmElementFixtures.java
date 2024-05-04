package com.github.pathfinder.indexer.configuration;

import java.util.Map;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.data.osm.OsmTags;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OsmElementFixtures {

    public OsmNode randomNode(long id, Map<String, String> tags) {
        return new OsmNode(id, randomCoordinate(), new OsmTags(tags));
    }

    public Coordinate randomCoordinate() {
        return new Coordinate(Math.random(), Math.random());
    }

}
