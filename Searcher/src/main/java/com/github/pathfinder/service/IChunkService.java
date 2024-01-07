package com.github.pathfinder.service;

import com.github.pathfinder.database.node.ChunkNode;
import java.util.List;

public interface IChunkService {

    ChunkNode save(ChunkNode node);

    List<ChunkNode> saveAll(List<ChunkNode> nodes);

    List<ChunkNode> chunks(List<Integer> ids);

}
