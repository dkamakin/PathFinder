package com.github.pathfinder.indexer.client.osm.impl;

import java.util.List;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.data.osm.OsmElementType;
import com.github.pathfinder.indexer.data.osm.OsmQueryTag;

public class OverpassQueryBuilder implements OsmClient.IOverpassQueryBuilder {

    private final OverpassQueryPartsBuilder builder;

    public OverpassQueryBuilder() {
        this.builder = new OverpassQueryPartsBuilder();
    }

    @Override
    public OsmClient.IOverpassQueryBuilder nodes(List<Long> ids) {
        return performAndReturnThis(() -> builder.appendNodes(ids));
    }

    @Override
    public OsmClient.IOverpassQueryBuilder element(OsmElementType element, OsmBox box, List<OsmQueryTag> tags) {
        return performAndReturnThis(() -> tags.forEach(tag -> builder.appendGeometry(element, box, tag)));
    }

    @Override
    public OsmClient.IOverpassQueryBuilder node(OsmBox box, List<OsmQueryTag> tags) {
        return performAndReturnThis(() -> element(OsmElementType.NODE, box, tags));
    }

    @Override
    public OsmClient.IOverpassQueryBuilder way(OsmBox box, List<OsmQueryTag> tags) {
        return performAndReturnThis(() -> element(OsmElementType.WAY, box, tags));
    }

    @Override
    public String asBody() {
        return builder.asBody();
    }

    @Override
    public String asCount() {
        return builder.asCount();
    }

    private OsmClient.IOverpassQueryBuilder performAndReturnThis(Runnable action) {
        action.run();
        return this;
    }

}
