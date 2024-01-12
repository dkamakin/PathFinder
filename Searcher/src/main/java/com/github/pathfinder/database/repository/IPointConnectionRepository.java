package com.github.pathfinder.database.repository;

import com.github.pathfinder.data.connection.PointConnectionStatistics;
import java.util.List;
import java.util.Optional;

public interface IPointConnectionRepository {

    Optional<PointConnectionStatistics> createConnections(List<Integer> chunkIds, Double accuracyMeters);

}
