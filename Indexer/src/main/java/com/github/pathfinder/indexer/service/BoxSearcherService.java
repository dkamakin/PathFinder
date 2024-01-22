package com.github.pathfinder.indexer.service;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface BoxSearcherService {

    List<IndexBoxEntity> all();

    List<IndexBoxEntity> savable(Duration saveDelay);

    List<IndexBoxEntity> connectable(Duration connectDelay);

    Optional<IndexBoxEntity> box(Integer id);

}
