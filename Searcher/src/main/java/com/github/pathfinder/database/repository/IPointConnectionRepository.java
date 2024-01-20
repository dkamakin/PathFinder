package com.github.pathfinder.database.repository;

import com.github.pathfinder.data.connection.IterateStatistics;
import java.util.Optional;

public interface IPointConnectionRepository {

    Optional<IterateStatistics> connectPointsInChunk(Integer chunkId, Double accuracyMeters);

    Optional<IterateStatistics> connectChunkBoarders(Integer chunkId, Double accuracyMeters);

}
