package com.github.pathfinder.indexer.client.osm.impl;

import java.util.List;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.data.osm.OsmElementType;
import com.github.pathfinder.indexer.data.osm.OsmQueryTag;
import com.github.pathfinder.indexer.tools.OsmTools;

public class OverpassQueryPartsBuilder {

    private static final char   REGEX                  = '~';
    private static final String REGEX_OR_DELIMITER     = "|";
    private static final char   OPENING_BRACKET        = '(';
    private static final char   CLOSING_BRACKET        = ')';
    private static final char   OPENING_SQUARE_BRACKET = '[';
    private static final char   CLOSING_SQUARE_BRACKET = ']';
    private static final char   DELIMITER              = ';';
    private static final char   QUOTE                  = '"';
    private static final char   COMMA                  = ',';
    private static final String OUT_BODY               = "out body";
    private static final String OUT_COUNT              = "out count";

    private final StringBuilder query;

    public OverpassQueryPartsBuilder() {
        this.query = new StringBuilder().append(OPENING_BRACKET);
    }

    public OverpassQueryPartsBuilder appendGeometry(OsmElementType element, OsmBox box, OsmQueryTag tag) {
        return appendGeometry(element.getOverpassValue(), box)
                .append(OPENING_SQUARE_BRACKET)
                .append(tag.name())
                .append(REGEX)
                .append(QUOTE)
                .append(() -> append(String.join(REGEX_OR_DELIMITER, tag.values())))
                .append(QUOTE)
                .append(CLOSING_SQUARE_BRACKET)
                .append(DELIMITER);
    }

    public OverpassQueryPartsBuilder appendNodes(List<Long> ids) {
        ids.forEach(this::appendNode);
        return this;
    }

    public void appendNode(Long id) {
        append(OsmElementType.NODE.getOverpassValue())
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

    private OverpassQueryPartsBuilder appendGeometry(String geometry, OsmBox box) {
        return append(geometry)
                .append(OPENING_BRACKET)
                .append(OsmTools.latitudeLongitude(box.min()))
                .append(COMMA)
                .append(OsmTools.latitudeLongitude(box.max()))
                .append(CLOSING_BRACKET);
    }

    @Override
    public String toString() {
        return query.toString();
    }

    private OverpassQueryPartsBuilder append(Runnable action) {
        action.run();
        return this;
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
