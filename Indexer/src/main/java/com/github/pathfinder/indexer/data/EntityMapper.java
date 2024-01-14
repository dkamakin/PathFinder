package com.github.pathfinder.indexer.data;

import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {

    EntityMapper MAPPER = Mappers.getMapper(EntityMapper.class);

    OsmBox osmBox(IndexBoxEntity entity);

}
