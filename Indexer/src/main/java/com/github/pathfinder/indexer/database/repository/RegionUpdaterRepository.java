package com.github.pathfinder.indexer.database.repository;

import com.github.pathfinder.indexer.database.entity.RegionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionUpdaterRepository extends CrudRepository<RegionEntity, Integer> {

}
