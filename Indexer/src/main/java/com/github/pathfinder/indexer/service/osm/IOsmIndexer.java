package com.github.pathfinder.indexer.service.osm;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;

public interface IOsmIndexer {

    void process(IndexBoxEntity box);

}
