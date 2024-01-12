package com.github.pathfinder.indexer.data.osm;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OsmTags {

    private final Map<String, String> tags;

    public Optional<String> get(String key) {
        return Optional.ofNullable(tags.get(key));
    }

}
