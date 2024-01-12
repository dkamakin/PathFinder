package com.github.pathfinder.database.repository;

import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunkGetterRepository  extends Neo4jRepository<ChunkNode, String> {

    List<SimpleChunk> findAllByIdIn(List<Integer> ids);

}
