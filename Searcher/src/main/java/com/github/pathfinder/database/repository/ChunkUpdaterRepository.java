package com.github.pathfinder.database.repository;

import com.github.pathfinder.database.node.ChunkNode;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunkUpdaterRepository extends Neo4jRepository<ChunkNode, String> {

    @Query("""
            MATCH (chunk:Chunk)
            WHERE chunk.id IN $ids
            SET chunk.connected = $connected
            """)
    void markConnected(@Param("ids") List<Integer> ids,
                       @Param("connected") boolean connected);

}
