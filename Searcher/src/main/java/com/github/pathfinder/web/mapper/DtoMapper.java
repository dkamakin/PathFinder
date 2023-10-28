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

    FoundPathDto map(AStarResult aStarResult);

    List<CoordinateDto> map(List<PointNode> points);

    FindPathRequest map(FindPathDto dto);

}
