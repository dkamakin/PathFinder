package com.github.pathfinder.indexer.service;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;

public interface Indexer {

    void process(IndexBoxEntity box);

}
