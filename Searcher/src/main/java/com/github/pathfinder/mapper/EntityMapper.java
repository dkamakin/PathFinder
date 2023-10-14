package com.github.pathfinder.mapper;

import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.entity.PointEntity;
import com.github.pathfinder.database.entity.PointRelation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {

    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "relations", source = "connections")
    PointEntity pointEntity(Point point);

    @Mapping(target = "id", ignore = true)
    PointRelation pointRelation(Point.PointConnection connection);

}
