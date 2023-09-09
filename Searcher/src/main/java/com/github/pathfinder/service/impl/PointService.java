package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.point.IndexedPoint;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.repository.PointRepository;
import com.github.pathfinder.mapper.EntityMapper;
import com.github.pathfinder.service.IPointService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService implements IPointService {

    private final PointRepository pointRepository;

    @Logged
    @Override
    @Transactional
    public IndexedPoint save(Point point) {
        var mapper = EntityMapper.INSTANCE;

        return mapper.map(pointRepository.save(mapper.map(point)));
    }

    @Logged
    @Override
    @ReadTransactional
    public Optional<IndexedPoint> findNearest(Coordinate coordinate) {
        return pointRepository
                .findNearest(coordinate.latitude(), coordinate.longitude())
                .map(EntityMapper.INSTANCE::map);
    }
}
