package com.github.pathfinder.indexer.data;

import com.github.pathfinder.indexer.data.index.IndexBox;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OsmMapper {

    OsmMapper INSTANCE = Mappers.getMapper(OsmMapper.class);

    OsmBox osmBox(IndexBox box);

}
