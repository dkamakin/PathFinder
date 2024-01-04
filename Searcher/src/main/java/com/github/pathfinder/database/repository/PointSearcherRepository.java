package com.github.pathfinder.database.repository;

import com.github.pathfinder.database.node.PointNode;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointSearcherRepository extends Neo4jRepository<PointNode, String> {

    @Query("""
            MATCH (node:Point)
            WITH node, point.distance(node.location2d, point({latitude: $latitude, longitude: $longitude})) AS distance
            WHERE distance <= $accuracyMeters
            RETURN node
            ORDER BY distance
            LIMIT 1
            """)
    Optional<PointNode> findNearest(@Param("latitude") Double latitude,
                                    @Param("longitude") Double longitude,
                                    @Param("accuracyMeters") Double accuracyMeters);

}
