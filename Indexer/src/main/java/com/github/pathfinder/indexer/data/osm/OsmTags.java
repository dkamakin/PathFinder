package com.github.pathfinder.indexer.data.osm;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OsmTags {

    private static final String NATURAL = "natural";

    private final Map<String, String> tags;

    public Optional<String> natural() {
        return Optional.ofNullable(tags.get(OsmTags.NATURAL));
    }
}
