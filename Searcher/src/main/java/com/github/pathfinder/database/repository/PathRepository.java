package com.github.pathfinder.database.repository;

import com.github.pathfinder.database.entity.PointEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathRepository extends Neo4jRepository<PointEntity, Long> {

}
