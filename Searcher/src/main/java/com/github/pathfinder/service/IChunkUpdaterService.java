package com.github.pathfinder.service;

import com.github.pathfinder.database.node.ChunkNode;

public interface IChunkUpdaterService {

    ChunkNode save(ChunkNode node);

    void markConnected(Integer id, boolean connected);

}
