package com.github.pathfinder.service;

import com.github.pathfinder.database.node.PointNode;
import java.util.List;

public interface IPointService {

    List<PointNode> saveAll(int chunkId, List<PointNode> nodes);

}
