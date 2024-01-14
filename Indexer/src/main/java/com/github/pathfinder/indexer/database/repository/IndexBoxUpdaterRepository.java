package com.github.pathfinder.indexer.database.repository;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexBoxUpdaterRepository extends CrudRepository<IndexBoxEntity, Integer> {

}
