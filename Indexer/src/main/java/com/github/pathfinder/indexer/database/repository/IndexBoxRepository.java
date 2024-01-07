package com.github.pathfinder.indexer.database.repository;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import java.time.Duration;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexBoxRepository extends CrudRepository<IndexBoxEntity, Integer> {

    @Query("""
            SELECT box
            FROM IndexBoxEntity box
            WHERE NOT box.saved OR NOT box.connected OR
            (box.connectionRequestTimestamp IS NOT NULL AND now() - box.connectionRequestTimestamp = :connectDelay) OR
            (box.saveRequestTimestamp IS NOT NULL AND now() - box.saveRequestTimestamp = :saveDelay)
            """)
    List<IndexBoxEntity> notSavedOrConnected(@Param("saveDelay") Duration saveDelay,
                                             @Param("connectDelay") Duration connectDelay);

}
