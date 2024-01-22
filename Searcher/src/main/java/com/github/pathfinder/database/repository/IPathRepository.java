package com.github.pathfinder.database.repository;

import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.data.path.AStarResult;
import java.util.Optional;

public interface IPathRepository {

    Optional<AStarResult> aStar(Coordinate source, Coordinate target);

}
