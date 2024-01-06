package com.github.pathfinder.indexer.client.osm.impl;

import com.github.pathfinder.indexer.configuration.osm.IndexBox;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OverpassQueryTools {

    private static final String NODE            = "node";
    private static final String OPENING_BRACKET = "(";
    private static final String CLOSING_BRACKET = ")";
    public static final  String DELIMITER       = ";";
    private static final String COMMA           = ",";

    public static String nodeQuery(Long id) {
        return NODE + OPENING_BRACKET + id + CLOSING_BRACKET;
    }

    public static String nodeQueries(List<Long> ids) {
        return ids.stream()
                .map(OverpassQueryTools::nodeQuery)
                .collect(Collectors.joining(DELIMITER));
    }

    public static String latitudeLongitude(IndexBox.BoxCoordinate box) {
        return box.getLatitude() + COMMA + box.getLongitude();
    }

}
