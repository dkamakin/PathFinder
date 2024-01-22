package com.github.pathfinder.indexer.client.osm.westnordost;

import com.github.pathfinder.indexer.data.osm.OsmElement;
import de.westnordost.osmapi.map.handler.MapDataHandler;
import java.util.List;

public interface WestNordOstHandler<T extends OsmElement> extends MapDataHandler {

    List<T> result();

}
