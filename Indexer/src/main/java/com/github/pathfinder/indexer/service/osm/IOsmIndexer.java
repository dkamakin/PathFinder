package com.github.pathfinder.indexer.service.osm;

import com.github.pathfinder.indexer.data.index.IndexBox;

public interface IOsmIndexer {

    void process(IndexBox box);

}
