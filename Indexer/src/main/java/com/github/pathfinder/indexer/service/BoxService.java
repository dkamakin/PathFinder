package com.github.pathfinder.indexer.service;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import java.util.List;
import java.util.Optional;

public interface BoxService {

    List<IndexBoxEntity> notSavedOrConnected();

    Optional<IndexBoxEntity> box(Integer id);

    List<IndexBoxEntity> boxes(List<Integer> ids);

}
