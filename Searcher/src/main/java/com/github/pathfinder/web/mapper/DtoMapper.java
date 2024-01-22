package com.github.pathfinder.web.mapper;

import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.web.dto.CoordinateDto;
import com.github.pathfinder.web.dto.path.FindPathDto;
import com.github.pathfinder.web.dto.path.FoundPathDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DtoMapper {

    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    FoundPathDto foundPathDto(AStarResult aStarResult);

    List<CoordinateDto> coordinates(List<PointNode> points);

    default CoordinateDto coordinateDto(PointNode node) {
        return new CoordinateDto(node.longitude(), node.latitude());
    }

    FindPathRequest findPathRequest(FindPathDto dto);

}
