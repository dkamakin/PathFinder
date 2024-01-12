package com.github.pathfinder.configuration.repository;

import com.github.pathfinder.database.node.ChunkNode;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestChunkNodeRepository extends Neo4jRepository<ChunkNode, String> {

    List<ChunkNode> findAllByIdIn(List<Integer> ids);

}
