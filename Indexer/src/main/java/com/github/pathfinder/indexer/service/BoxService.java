package com.github.pathfinder.indexer.service;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface BoxService {

    List<IndexBoxEntity> notSavedOrConnected();

    List<IndexBoxEntity> notSavedOrConnected(Duration saveDelay, Duration connectDelay);

    Optional<IndexBoxEntity> box(Integer id);

    List<IndexBoxEntity> boxes(List<Integer> ids);

}
