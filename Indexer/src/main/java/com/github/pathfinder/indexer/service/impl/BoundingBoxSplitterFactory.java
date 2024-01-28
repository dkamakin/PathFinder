package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.tools.impl.MetersDistance;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.SplitterConfiguration;
import com.github.pathfinder.indexer.service.BoundingBoxSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@RequiredArgsConstructor
public class BoundingBoxSplitterFactory {

    private final SplitterConfiguration splitterConfiguration;
    private final OsmClient             osmClient;

    public BoundingBoxSplitter create() {
        return new BoundingBoxOsmElementsSplitter(splitterConfiguration.getElementsLimit(),
                                                  new MetersDistance(splitterConfiguration.getAdditionalSpaceMeters()),
                                                  osmClient);
    }

}
