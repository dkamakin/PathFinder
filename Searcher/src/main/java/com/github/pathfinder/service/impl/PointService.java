package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.repository.PointRepository;
import com.github.pathfinder.mapper.NodeMapper;
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
    public PointNode save(Point point) {
        return save(NodeMapper.INSTANCE.pointNode(point));
    }

    @Override
    @Transactional
    @Logged("point")
    public PointNode save(PointNode point) {
        return pointRepository.save(point);
    }

}
