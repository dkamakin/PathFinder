package com.github.pathfinder.indexer.client.osm.impl;

import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import java.util.List;

public class OverpassQueryBuilder implements OsmClient.IOverpassQueryBuilder {

    private final OverpassQueryPartsBuilder builder;

    public OverpassQueryBuilder() {
        this.builder = new OverpassQueryPartsBuilder();
    }

    @Override
    public OsmClient.IOverpassQueryBuilder nodes(List<Long> ids) {
        return performAndReturnThis(() -> builder.nodes(ids));
    }

    @Override
    public OsmClient.IOverpassQueryBuilder node(OsmBox box) {
        return performAndReturnThis(() -> builder.node(box));
    }

    @Override
    public OsmClient.IOverpassQueryBuilder way(OsmBox box) {
        return performAndReturnThis(() -> builder.way(box));
    }

    @Override
    public String asBody() {
        return builder.asBody();
    }

    private OsmClient.IOverpassQueryBuilder performAndReturnThis(Runnable action) {
        action.run();
        return this;
    }

}
