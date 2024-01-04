package com.github.pathfinder.indexer.client.osm.impl;

import com.github.pathfinder.indexer.client.osm.OsmClient;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class OverpassQueryBuilder implements OsmClient.IOverpassQueryBuilder {

    private static final char PREFIX = '$';

    private final String query;

    @Override
    public String bind(Map<String, Object> arguments) {
        var result = query;

        for (var entry : arguments.entrySet()) {
            result = StringUtils.replace(result, PREFIX + entry.getKey(), entry.getValue().toString());
        }

        return result;
    }

}
