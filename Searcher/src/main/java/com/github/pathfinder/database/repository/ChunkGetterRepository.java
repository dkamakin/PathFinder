package com.github.pathfinder.database.repository;

import java.util.List;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunkGetterRepository extends Neo4jRepository<ChunkNode, String> {

    List<SimpleChunk> findAllByIdIn(List<Integer> ids);

    boolean existsById(int id);

}
