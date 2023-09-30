package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.entity.PointEntity;
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
    public PointEntity save(Point point) {
        return pointRepository.save(EntityMapper.INSTANCE.map(point));
    }

    @Logged
    @Override
    @ReadTransactional
    public Optional<PointEntity> findNearest(Coordinate coordinate) {
        return pointRepository.findNearest(coordinate.latitude(), coordinate.longitude());
    }
}
