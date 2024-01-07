package com.github.pathfinder.indexer.service;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import java.util.List;

public interface IActualizeService {

    List<IndexBoxEntity> perform();

}
