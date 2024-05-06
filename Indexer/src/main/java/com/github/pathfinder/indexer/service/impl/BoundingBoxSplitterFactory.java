package com.github.pathfinder.indexer.service.impl;

import static com.github.pathfinder.indexer.data.OsmMapper.MAPPER;
import com.github.pathfinder.core.data.MetersDistance;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.SplitterConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfigurationProperties;
import com.github.pathfinder.indexer.service.BoundingBoxSplitter;
import com.github.pathfinder.indexer.service.osm.impl.BoundingBoxOsmElementsSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@RequiredArgsConstructor
public class BoundingBoxSplitterFactory {

    private final SplitterConfiguration      splitterConfiguration;
    private final OsmClient                  osmClient;
    private final OsmConfigurationProperties osmConfigurationProperties;

    public BoundingBoxSplitter create() {
        return BoundingBoxOsmElementsSplitter.builder()
                .elementsLimit(splitterConfiguration.getElementsLimit())
                .osmClient(osmClient)
                .additionalSpace(new MetersDistance(splitterConfiguration.getAdditionalSpaceMeters()))
                .tags(MAPPER.osmQueryTags(osmConfigurationProperties.getTags()))
                .build();
    }

}
