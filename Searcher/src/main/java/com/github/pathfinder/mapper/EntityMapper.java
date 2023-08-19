package com.github.pathfinder.mapper;

import com.github.pathfinder.data.point.IndexedPoint;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.entity.PointEntity;

public class EntityMapper {

    public static PointEntity map(Point point) {
        return new PointEntity(point.getAltitude(), point.getLongitude(), point.getLatitude(), point.getLandType());
    }

    public static IndexedPoint map(PointEntity entity) {
        return new IndexedPoint(entity.getAltitude(), entity.getLongitude(), entity.getLatitude(),
                                entity.getLandType(),
                                entity.getId());
    }

}
