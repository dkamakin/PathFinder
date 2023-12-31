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
            MATCH (point:Point)
            WITH point, (abs($latitude - point.latitude) + abs($longitude  - point.longitude)) as distanceToExpected
            RETURN point
            ORDER BY distanceToExpected
            LIMIT 1
            """)
    Optional<PointNode> findNearest(@Param("latitude") Double latitude,
                                    @Param("longitude") Double longitude);

}
