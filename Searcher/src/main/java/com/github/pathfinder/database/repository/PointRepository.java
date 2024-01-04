package com.github.pathfinder.database.repository;

import com.github.pathfinder.database.node.PointNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends Neo4jRepository<PointNode, String> {

    @Query("""
            MATCH (node:Point)
            MATCH (existing:Point)
              WHERE node.id <> existing.id
            WITH node, existing, point.distance(existing.location3d, node.location3d) AS distanceMeters
              WHERE distanceMeters <= $accuracy
            WITH node, existing, distanceMeters, ((node.passabilityCoefficient + existing.passabilityCoefficient) / 2) * distanceMeters AS weight
            MERGE (existing)-[:CONNECTION {distanceMeters: distanceMeters, weight: weight}]->(node)
                  """)
    void createConnections(@Param("accuracy") Double accuracy);

}
