package com.github.pathfinder.indexer.configuration;

import com.github.pathfinder.indexer.configuration.osm.OsmConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfiguration.OsmTagConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfiguration.OsmTagValue;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class OsmTestTemplate {

    private final OsmConfiguration osmConfiguration;

    public OsmTagConfiguration tagConfiguration(int index) {
        return osmConfiguration.getTags().stream().toList().get(index);
    }

    public OsmLandType supportedLandType(OsmTagValue tagConfiguration) {
        return new OsmLandType(tagConfiguration.name(), tagConfiguration.weight());
    }

    public OsmLandType supportedLandType() {
        return supportedLandType(0);
    }

    public OsmLandType supportedLandType(int index) {
        return supportedLandType(tagConfiguration(0).values().stream().toList().get(index));
    }

    public String supportedKey() {
        return supportedKey(0);
    }

    public String supportedKey(int index) {
        return osmConfiguration.getTags().stream().toList().get(index).name();
    }

}
