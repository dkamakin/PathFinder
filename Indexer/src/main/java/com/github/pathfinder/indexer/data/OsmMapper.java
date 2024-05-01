package com.github.pathfinder.indexer.data;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.indexer.configuration.osm.OsmConfiguration.OsmTagConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfiguration.OsmTagValue;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
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

}
