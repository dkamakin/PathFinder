package com.github.pathfinder.indexer.client.osm;

import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import java.util.List;

public interface OsmClient {

    List<OsmNode> nodes(List<Long> ids);

    List<OsmElement> elements(OsmBox box);

    interface IOverpassQueryBuilder {

        IOverpassQueryBuilder nodes(List<Long> ids);

        IOverpassQueryBuilder node(OsmBox box);

        IOverpassQueryBuilder way(OsmBox box);

        String asBody();

    }

}
