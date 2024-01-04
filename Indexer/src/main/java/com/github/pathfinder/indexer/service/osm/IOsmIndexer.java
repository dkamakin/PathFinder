package com.github.pathfinder.indexer.service.osm;

import com.github.pathfinder.indexer.configuration.osm.IndexBox;

public interface IOsmIndexer {

    void process(IndexBox box);

}
