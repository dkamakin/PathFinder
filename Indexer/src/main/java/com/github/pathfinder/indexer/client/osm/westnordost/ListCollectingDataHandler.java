package com.github.pathfinder.indexer.client.osm.westnordost;

import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.google.common.collect.Lists;
import de.westnordost.osmapi.map.data.BoundingBox;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Relation;
import de.westnordost.osmapi.map.data.Way;
import de.westnordost.osmapi.map.handler.MapDataHandler;
import java.util.List;
import java.util.function.BiFunction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ListCollectingDataHandler implements MapDataHandler {

    private final List<OsmElement> elements;

    public ListCollectingDataHandler() {
        this.elements = Lists.newArrayList();
    }

    @Override
    public void handle(BoundingBox bounds) {
        log.info("Handling a bounding box: {}", bounds);
    }

    @Override
    public void handle(Node node) {
        add(node, WestNordOstMapper::osmNode);
    }

    @Override
    public void handle(Way way) {
        add(way, WestNordOstMapper::osmWay);
    }

    @Override
    public void handle(Relation relation) {
        // relations are not supported
    }

    private <T> void add(T element, BiFunction<WestNordOstMapper, T, OsmElement> mapper) {
        add(mapper.apply(WestNordOstMapper.MAPPER, element));
    }

    private void add(OsmElement element) {
        elements.add(element);
    }

}
