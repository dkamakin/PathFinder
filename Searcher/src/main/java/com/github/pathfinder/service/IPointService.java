package com.github.pathfinder.service;

import com.github.pathfinder.database.node.PointNode;
import java.util.List;

public interface IPointService {

    List<PointNode> saveAll(Integer chunkId, List<PointNode> nodes);

    void createConnections(List<Integer> chunkIds);

}
