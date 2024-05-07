package com.github.pathfinder.indexer.client.osm;

import java.util.List;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmElementType;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.data.osm.OsmQueryTag;

public interface OsmClient {

    List<OsmNode> nodes(List<Long> ids);

    List<OsmElement> elements(OsmBox box, List<OsmQueryTag> tags);

    List<OsmElement> ways(OsmBox box, List<OsmQueryTag> tags);

    long count(OsmElementType element, OsmBox box, List<OsmQueryTag> tags);

    interface IOverpassQueryBuilder {

        IOverpassQueryBuilder nodes(List<Long> ids);

        IOverpassQueryBuilder element(OsmElementType element, OsmBox box, List<OsmQueryTag> tags);

        IOverpassQueryBuilder node(OsmBox box, List<OsmQueryTag> tags);

        IOverpassQueryBuilder way(OsmBox box, List<OsmQueryTag> tags);

        String asBody();

        String asCount();

    }

}
