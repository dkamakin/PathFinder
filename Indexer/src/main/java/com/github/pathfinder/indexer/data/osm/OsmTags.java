package com.github.pathfinder.indexer.data.osm;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OsmTags {

    private final Map<String, String> tags;

    public Optional<String> get(String key) {
        return Optional.ofNullable(tags.get(key));
    }

    public Set<Map.Entry<String, String>> entries() {
        return tags.entrySet();
    }

}
