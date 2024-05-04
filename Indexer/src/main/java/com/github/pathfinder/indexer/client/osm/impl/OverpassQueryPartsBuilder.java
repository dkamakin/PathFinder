package com.github.pathfinder.indexer.client.osm.impl;

import java.util.List;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.tools.OsmTools;

public class OverpassQueryPartsBuilder {

    private static final String NODE            = "node";
    private static final String WAY             = "way";
    private static final char   OPENING_BRACKET = '(';
    private static final char   CLOSING_BRACKET = ')';
    public static final  char   DELIMITER       = ';';
    private static final char   COMMA           = ',';
    private static final String OUT_BODY        = "out body";
    private static final String OUT_COUNT       = "out count";

    private final StringBuilder query;

    public OverpassQueryPartsBuilder() {
        this.query = new StringBuilder().append(OPENING_BRACKET);
    }

    public OverpassQueryPartsBuilder way(OsmBox box) {
        return geometry(WAY, box);
    }

    public OverpassQueryPartsBuilder node(OsmBox box) {
        return geometry(NODE, box);
    }

    public OverpassQueryPartsBuilder nodes(List<Long> ids) {
        ids.forEach(this::appendNode);
        return this;
    }

    public void appendNode(Long id) {
        append(NODE)
                .append(OPENING_BRACKET)
                .append(id.toString())
                .append(CLOSING_BRACKET)
                .append(DELIMITER);
    }

    public String asBody() {
        return append(CLOSING_BRACKET).append(DELIMITER).append(OUT_BODY).append(DELIMITER).toString();
    }

    public String asCount() {
        return append(CLOSING_BRACKET).append(DELIMITER).append(OUT_COUNT).append(DELIMITER).toString();
    }

    private OverpassQueryPartsBuilder geometry(String geometry, OsmBox box) {
        return append(geometry)
                .append(OPENING_BRACKET)
                .append(OsmTools.latitudeLongitude(box.min()))
                .append(COMMA)
                .append(OsmTools.latitudeLongitude(box.max()))
                .append(CLOSING_BRACKET)
                .append(DELIMITER);
    }

    @Override
    public String toString() {
        return query.toString();
    }

    private OverpassQueryPartsBuilder append(char aChar) {
        query.append(aChar);
        return this;
    }

    private OverpassQueryPartsBuilder append(String string) {
        query.append(string);
        return this;
    }

}
