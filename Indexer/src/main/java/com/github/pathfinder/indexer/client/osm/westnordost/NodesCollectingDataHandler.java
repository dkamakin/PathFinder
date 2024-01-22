package com.github.pathfinder.indexer.client.osm.westnordost;

import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.google.common.collect.Lists;
import de.westnordost.osmapi.map.data.BoundingBox;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Relation;
import de.westnordost.osmapi.map.data.Way;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NodesCollectingDataHandler implements WestNordOstHandler<OsmNode> {

    private final List<OsmNode> elements;

    public NodesCollectingDataHandler() {
        this.elements = Lists.newArrayList();
    }

    @Override
    public void handle(BoundingBox bounds) {
        log.info("Handling a bounding box: {}", bounds);
    }

    @Override
    public void handle(Node node) {
        elements.add(WestNordOstMapper.MAPPER.osmNode(node));
    }

    @Override
    public void handle(Way way) {
        // only nodes are supported
    }

    @Override
    public void handle(Relation relation) {
        // only nodes are supported
    }

    @Override
    public List<OsmNode> result() {
        return elements;
    }

}
