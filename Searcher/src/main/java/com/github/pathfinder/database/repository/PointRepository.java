package com.github.pathfinder.database.repository;

import com.github.pathfinder.database.node.PointNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends Neo4jRepository<PointNode, String> {

}
