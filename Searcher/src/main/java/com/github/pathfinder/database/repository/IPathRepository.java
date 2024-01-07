package com.github.pathfinder.database.repository;

import com.github.pathfinder.data.path.AStarResult;
import java.util.Optional;
import java.util.UUID;

public interface IPathRepository {

    Optional<AStarResult> aStar(String graphName, UUID sourceId, UUID targetId);

}
