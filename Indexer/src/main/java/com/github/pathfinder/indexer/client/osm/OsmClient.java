package com.github.pathfinder.indexer.client.osm;

import com.github.pathfinder.indexer.configuration.osm.IndexBox;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import java.util.List;
import java.util.Map;

public interface OsmClient {

    List<OsmNode> nodes(List<Long> ids);

    List<OsmElement> elements(IndexBox box);

    interface IOverpassQueryBuilder {

        String bind(Map<String, Object> arguments);

    }

}
