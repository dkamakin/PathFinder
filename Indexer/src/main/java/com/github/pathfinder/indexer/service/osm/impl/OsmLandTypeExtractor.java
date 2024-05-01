package com.github.pathfinder.indexer.service.osm.impl;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.github.pathfinder.indexer.configuration.osm.OsmConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfiguration.OsmTagConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfiguration.OsmTagValue;
import com.github.pathfinder.indexer.data.OsmMapper;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import com.github.pathfinder.indexer.data.osm.OsmTags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RefreshScope
public class OsmLandTypeExtractor {

    private final Map<String, Map<String, OsmTagValue>> tagsIndex;

    public OsmLandTypeExtractor(OsmConfiguration configuration) {
        this.tagsIndex = configuration.getTags().stream()
                .collect(Collectors.toMap(OsmTagConfiguration::name, OsmMapper.MAPPER::tagsIndex));

        log.info("Initialized an extractor, configuration: {}", configuration);
    }

    public boolean hasLandType(OsmElement osmElement) {
        return from(osmElement.tags()).isPresent();
    }

    private Optional<OsmTagValue> osmTagValue(Map.Entry<String, String> entryTag) {
        return Optional.ofNullable(tagsIndex.get(entryTag.getKey())).map(tag -> tag.get(entryTag.getValue()));
    }

    public Optional<OsmLandType> from(OsmTags tags) {
        return tags.entries().stream()
                .map(this::osmTagValue)
                .flatMap(Optional::stream)
                .map(OsmMapper.MAPPER::osmLandType)
                .findFirst();
    }

}
