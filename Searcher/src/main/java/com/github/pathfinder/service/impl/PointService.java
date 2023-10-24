package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.entity.PointEntity;
import com.github.pathfinder.database.repository.PointRepository;
import com.github.pathfinder.mapper.EntityMapper;
import com.github.pathfinder.service.IPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService implements IPointService {

    private final PointRepository pointRepository;

    @Override
    @Transactional
    @Logged("point")
    public PointEntity save(Point point) {
        return pointRepository.save(EntityMapper.INSTANCE.pointEntity(point));
    }

    @Override
    @Transactional
    @Logged("point")
    public PointEntity save(PointEntity point) {
        return pointRepository.save(point);
    }

}
