package com.github.pathfinder.indexer.database.repository;

import com.github.pathfinder.indexer.database.entity.RegionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionSearcherRepository extends CrudRepository<RegionEntity, Integer> {

    @Query("""
            SELECT region
            FROM RegionEntity region
            WHERE NOT region.processed
            ORDER BY region.id
            LIMIT 1
            """)
    Optional<RegionEntity> nextNotProcessed();

}
