package com.github.pathfinder.indexer.configuration;

import com.github.pathfinder.indexer.configuration.osm.OsmLandTypeConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmLandTypeConfiguration.OsmTagConfiguration;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class OsmTestTemplate {

    private final OsmLandTypeConfiguration landTypeConfiguration;

    public OsmTagConfiguration tagConfiguration(int index) {
        return landTypeConfiguration.getValues().stream().toList().get(index);
    }

    public OsmLandType supportedLandType(OsmTagConfiguration tagConfiguration) {
        return new OsmLandType(tagConfiguration.name(), tagConfiguration.weight());
    }

    public OsmLandType supportedLandType() {
        return supportedLandType(0);
    }

    public OsmLandType supportedLandType(int index) {
        return supportedLandType(tagConfiguration(index));
    }

    public String supportedKey() {
        return supportedKey(0);
    }

    public String supportedKey(int index) {
        return landTypeConfiguration.getKeys().stream().toList().get(index);
    }

}
