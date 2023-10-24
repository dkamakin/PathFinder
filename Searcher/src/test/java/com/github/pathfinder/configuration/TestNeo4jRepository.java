package com.github.pathfinder.configuration;

import com.github.pathfinder.database.entity.PointEntity;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TestNeo4jRepository extends Neo4jRepository<PointEntity, String> {

    @Query("CALL gds.graph.list() YIELD graphName RETURN graphName")
    List<String> graphNames();

}
