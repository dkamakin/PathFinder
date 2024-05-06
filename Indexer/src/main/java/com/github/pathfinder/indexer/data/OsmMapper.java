package com.github.pathfinder.indexer.data;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.indexer.configuration.osm.OsmConfigurationProperties.OsmTagConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfigurationProperties.OsmTagValue;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import com.github.pathfinder.indexer.data.osm.OsmQueryTag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OsmMapper {

    OsmMapper MAPPER = Mappers.getMapper(OsmMapper.class);

    OsmBox osmBox(BoundingBox box);

    OsmLandType osmLandType(OsmTagValue configuration);

    default Map<String, OsmTagValue> tagsIndex(OsmTagConfiguration tag) {
        return tag.values().stream().collect(Collectors.toMap(OsmTagValue::name, Function.identity()));
    }

    default List<OsmQueryTag> osmQueryTags(Set<OsmTagConfiguration> tagConfigurations) {
        return tagConfigurations.stream().map(OsmMapper.MAPPER::osmQueryTag).toList();
    }

    default OsmQueryTag osmQueryTag(OsmTagConfiguration tagConfiguration) {
        return new OsmQueryTag(tagConfiguration.name(), tagConfiguration.values().stream()
                .map(OsmTagValue::name)
                .toList());
    }

}
