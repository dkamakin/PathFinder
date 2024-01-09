package com.github.pathfinder.indexer.database.repository;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import java.time.Duration;
import java.time.Instant;
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
            WHERE
            NOT box.saved AND (box.saveRequestTimestamp IS NULL OR :now - box.saveRequestTimestamp > :saveDelay) OR
            NOT box.connected AND (box.connectionRequestTimestamp IS NULL OR :now - box.connectionRequestTimestamp > :connectDelay)
            """)
    List<IndexBoxEntity> notSavedOrConnected(@Param("saveDelay") Duration saveDelay,
                                             @Param("connectDelay") Duration connectDelay,
                                             @Param("now") Instant now);

    @Query("""
            SELECT box
            FROM IndexBoxEntity box
            WHERE NOT box.saved OR NOT box.connected
            """)
    List<IndexBoxEntity> notSavedOrConnected();

}
