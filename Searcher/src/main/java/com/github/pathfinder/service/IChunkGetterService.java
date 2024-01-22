package com.github.pathfinder.service;

import com.github.pathfinder.database.node.projection.SimpleChunk;
import java.util.List;

public interface IChunkGetterService {

    List<SimpleChunk> simple(List<Integer> ids);

}
