package com.github.pathfinder.database.repository;

import com.github.pathfinder.data.path.AStarResult;
import java.util.UUID;

public interface IPathRepository {

    AStarResult aStar(String graphName, UUID sourceId, UUID targetId);

}
