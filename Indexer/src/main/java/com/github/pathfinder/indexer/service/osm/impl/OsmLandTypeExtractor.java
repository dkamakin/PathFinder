package com.github.pathfinder.indexer.service.osm.impl;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import static com.github.pathfinder.indexer.configuration.osm.OsmLandTypeConfiguration.OsmTagConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmLandTypeConfiguration;
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

    private final OsmLandTypeConfiguration         configuration;
    private final Map<String, OsmTagConfiguration> valuesIndex;

    public OsmLandTypeExtractor(OsmLandTypeConfiguration configuration) {
        this.configuration = configuration;
        this.valuesIndex   = configuration.getValues().stream()
                .collect(Collectors.toMap(OsmTagConfiguration::name, Function.identity()));

        log.info("Initialized an extractor, configuration: {}", configuration);
    }

    public boolean hasLandType(OsmElement osmElement) {
        return from(osmElement.tags()).isPresent();
    }

    public Optional<OsmLandType> from(String type) {
        return Optional.ofNullable(valuesIndex.get(type)).map(OsmMapper.MAPPER::osmLandType);
    }

    public Optional<OsmLandType> from(OsmTags tags) {
        return configuration.getKeys().stream()
                .map(tags::get)
                .flatMap(Optional::stream)
                .map(this::from)
                .flatMap(Optional::stream)
                .findFirst();
    }

}
