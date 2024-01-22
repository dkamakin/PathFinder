package com.github.pathfinder.indexer.data.osm;

import com.github.pathfinder.core.data.Coordinate;
import com.google.common.base.Objects;

public record OsmNode(long id, Coordinate coordinate, OsmTags tags) implements OsmElement {

    @Override
    public OsmElementType type() {
        return OsmElementType.NODE;
    }

    @Override
    public OsmNode asNode() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OsmNode osmNode = (OsmNode) o;
        return id == osmNode.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
