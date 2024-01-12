package com.github.pathfinder.database.repository;

import com.github.pathfinder.database.node.PointNode;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointConnectionRepository extends Neo4jRepository<PointNode, String> {

    @Query("""
            CALL apoc.periodic.iterate(
            'MATCH (chunk:Chunk)-[:IN_CHUNK]-(first:Point) WHERE chunk.id IN $chunkIds RETURN first',
            'MATCH (second:Point) WHERE elementId(first) <> elementId(second) WITH first, second, point.distance(first.location3d, second.location3d) AS distanceMeters WHERE distanceMeters <= $accuracyMeters WITH first, second, distanceMeters, ((second.passabilityCoefficient + first.passabilityCoefficient) / 2) * distanceMeters AS weight MERGE (first)-[:CONNECTION {distanceMeters: distanceMeters, weight: weight}]->(second)',
            {batchSize: 1})
                              """)
    void createConnections(@Param("accuracyMeters") Double accuracyMeters,
                           @Param("chunkIds") List<Integer> chunkIds);

}
