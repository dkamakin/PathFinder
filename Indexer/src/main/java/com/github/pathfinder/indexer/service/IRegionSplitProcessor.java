package com.github.pathfinder.indexer.service;

import com.github.pathfinder.indexer.database.entity.RegionEntity;

public interface IRegionSplitProcessor {

    void split(RegionEntity entity);

}
