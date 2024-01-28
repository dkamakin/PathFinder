package com.github.pathfinder.indexer.service;

import com.github.pathfinder.indexer.database.entity.RegionEntity;
import java.util.Optional;

public interface IRegionSearcher {

    Optional<RegionEntity> nextNotProcessed();

}
