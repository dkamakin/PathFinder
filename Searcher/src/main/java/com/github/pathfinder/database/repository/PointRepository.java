package com.github.pathfinder.database.repository;

import com.github.pathfinder.database.entity.PointEntity;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends Neo4jRepository<PointEntity, Long> {

    @Query(value = """
            MATCH (p:Point)
            with p, (abs($latitude - p.latitude) + abs($longitude  - p.longitude)) as value
            return p order by value limit 1
                """)
    Optional<PointEntity> findNearest(@Param("latitude") Double latitude,
                                      @Param("longitude") Double longitude);

}
