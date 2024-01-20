package com.github.pathfinder.indexer.data;

import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.database.entity.MaxBoxCoordinate;
import com.github.pathfinder.indexer.database.entity.MinBoxCoordinate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {

    EntityMapper MAPPER = Mappers.getMapper(EntityMapper.class);

    OsmBox osmBox(IndexBoxEntity entity);

    Coordinate coordinate(MinBoxCoordinate min);

    Coordinate coordinate(MaxBoxCoordinate max);

}
