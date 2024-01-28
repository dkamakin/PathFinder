package com.github.pathfinder.indexer.data;

import com.github.pathfinder.core.tools.impl.BoundingBox;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OsmMapper {

    OsmMapper MAPPER = Mappers.getMapper(OsmMapper.class);

    OsmBox osmBox(BoundingBox box);

}
