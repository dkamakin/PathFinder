package com.github.pathfinder.indexer.data;

import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.indexer.configuration.osm.OsmLandTypeConfiguration;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OsmMapper {

    OsmMapper MAPPER = Mappers.getMapper(OsmMapper.class);

    OsmBox osmBox(BoundingBox box);

    OsmLandType osmLandType(OsmLandTypeConfiguration.OsmTagConfiguration configuration);

}
