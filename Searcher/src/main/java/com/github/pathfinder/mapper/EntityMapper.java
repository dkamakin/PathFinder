package com.github.pathfinder.mapper;

import com.github.pathfinder.data.point.IndexedPoint;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.entity.PointEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {

    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "connection", ignore = true)
    PointEntity map(Point point);

    @Mapping(target = "point", source = "entity")
    IndexedPoint map(PointEntity entity);

}
