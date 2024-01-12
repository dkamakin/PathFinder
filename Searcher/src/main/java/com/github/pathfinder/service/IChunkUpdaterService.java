package com.github.pathfinder.service;

import com.github.pathfinder.database.node.ChunkNode;
import java.util.List;

public interface IChunkUpdaterService {

    ChunkNode save(ChunkNode node);

    void markConnected(List<Integer> ids, boolean connected);

}
