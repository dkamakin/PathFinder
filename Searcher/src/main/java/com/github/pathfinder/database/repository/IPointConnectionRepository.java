package com.github.pathfinder.database.repository;

import com.github.pathfinder.data.connection.PointConnectionStatistics;
import java.util.Optional;

public interface IPointConnectionRepository {

    Optional<PointConnectionStatistics> createConnections(Integer chunkId, Double accuracyMeters);

}
