package com.github.pathfinder.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.database.entity.PointEntity;
import com.github.pathfinder.database.repository.PointSearcherRepository;
import com.github.pathfinder.service.IPointSearcherService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointSearcherService implements IPointSearcherService {

    private final PointSearcherRepository searcherRepository;

    @Override
    @ReadTransactional
    @Logged("coordinate")
    public Optional<PointEntity> findNearest(Coordinate coordinate) {
        return searcherRepository.findNearest(coordinate.latitude(), coordinate.longitude());
    }

}
