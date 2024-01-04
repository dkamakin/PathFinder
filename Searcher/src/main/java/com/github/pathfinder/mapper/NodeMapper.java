package com.github.pathfinder.mapper;

import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.searcher.api.data.point.Point;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NodeMapper {

    NodeMapper MAPPER = Mappers.getMapper(NodeMapper.class);

    default List<PointNode> pointNodes(List<Point> requests) {
        return requests.stream().map(this::pointNode).toList();
    }

    default PointNode pointNode(Point point) {
        return PointNode.builder()
                .location(point.coordinate().latitude(), point.coordinate().longitude(), point.altitude())
                .passabilityCoefficient(point.passabilityCoefficient())
                .landType(point.landType())
                .build();
    }

}
