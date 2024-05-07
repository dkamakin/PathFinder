package com.github.pathfinder.indexer.configuration;

import java.util.Optional;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionTestSearcherRepository extends CrudRepository<RegionEntity, Integer> {

    @Query("""
            SELECT region
            FROM RegionEntity region
            JOIN FETCH region.boxes
            WHERE region.id = :id
            """)
    Optional<RegionEntity> findEager(@Param("id") int id);

}
