package com.github.pathfinder.mapper;

import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.PointRelation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NodeMapper {

    NodeMapper INSTANCE = Mappers.getMapper(NodeMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "internalId", ignore = true)
    @Mapping(target = "relations", source = "connections")
    PointNode pointNode(Point point);

    @Mapping(target = "id", ignore = true)
    PointRelation pointRelation(Point.PointConnection connection);

}
