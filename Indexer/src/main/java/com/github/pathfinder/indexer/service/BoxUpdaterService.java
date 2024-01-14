package com.github.pathfinder.indexer.service;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;

public interface BoxUpdaterService {

    IndexBoxEntity save(IndexBoxEntity entity);

}
