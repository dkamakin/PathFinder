package com.github.pathfinder.indexer.data.elevation;

import com.github.pathfinder.core.data.Coordinate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ElevationMapper {

    ElevationMapper MAPPER = Mappers.getMapper(ElevationMapper.class);

    Coordinate coordinate(ElevationCoordinate coordinate);

    ElevationCoordinate elevationCoordinate(Coordinate coordinate);

    default Elevation elevation(OpenElevationResponse.OpenElevationPoint elevation) {
        return new Elevation(elevationCoordinate(elevation), elevation.elevation());
    }

    ElevationCoordinate elevationCoordinate(OpenElevationResponse.OpenElevationPoint elevation);

}
