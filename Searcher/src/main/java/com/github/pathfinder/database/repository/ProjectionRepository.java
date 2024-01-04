package com.github.pathfinder.database.repository;

import com.github.pathfinder.database.node.PointNode;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionRepository extends Neo4jRepository<PointNode, String> {

    @Query("CALL gds.graph.list() YIELD graphName RETURN graphName")
    List<String> all();

    @Query("""
            UNWIND $graphNames AS name
            CALL gds.graph.drop(
            name,
            false
            )
            YIELD graphName
            RETURN graphName
                        """)
    List<String> deleteAll(@Param("graphNames") List<String> graphNames);

    @Query(value = """
            CALL gds.graph.project(
            $graphName,
            'Point',
            'CONNECTION',
            {
              nodeProperties:         ['latitude', 'longitude'],
              relationshipProperties: 'weight'
            })
            YIELD nodeCount
            RETURN nodeCount
                        """)
    int createProjection(@Param("graphName") String graphName);

    @Query(value = """
            CALL gds.graph.exists($graphName)
            YIELD exists
            RETURN exists
            """, exists = true)
    boolean exists(@Param("graphName") String graphName);

}
