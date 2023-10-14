package com.github.pathfinder.database.repository;

import com.github.pathfinder.database.entity.PointEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionRepository extends Neo4jRepository<PointEntity, String> {

    @Query(value = """
            CALL gds.graph.project(
            $graphName,
            'Point',
            'CONNECTION',
            {
              nodeProperties:         ['latitude', 'longitude'],
              relationshipProperties: 'distance'
            }
            )
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

    @Query("""
            CALL gds.graph.drop($graphName)
            YIELD graphName
            RETURN graphName
            """)
    String delete(@Param("graphName") String graphName);

}
