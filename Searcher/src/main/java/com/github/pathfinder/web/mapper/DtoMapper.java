package com.github.pathfinder.web.mapper;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.HealthType;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.data.path.FindPathResponse;
import com.github.pathfinder.web.dto.CoordinateDto;
import com.github.pathfinder.web.dto.HealthTypeDto;
import com.github.pathfinder.web.dto.path.FindPathDto;
import com.github.pathfinder.web.dto.path.FoundPathDto;

public class DtoMapper {

    public static FoundPathDto map(FindPathResponse response) {
        return new FoundPathDto(response.cost());
    }

    public static FindPathRequest map(FindPathDto dto) {
        return new FindPathRequest(
                map(dto.source()),
                map(dto.target()),
                map(dto.health())
        );
    }

    public static HealthType map(HealthTypeDto dto) {
        return switch (dto) {
            case HEALTHY -> HealthType.HEALTHY;
            case WOUNDED -> HealthType.WOUNDED;
            case WEAKENED -> HealthType.WEAKENED;
        };
    }

    public static Coordinate map(CoordinateDto dto) {
        return new Coordinate(dto.longitude(), dto.latitude());
    }
}
