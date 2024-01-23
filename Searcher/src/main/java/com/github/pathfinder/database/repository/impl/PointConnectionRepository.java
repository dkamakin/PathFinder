package com.github.pathfinder.database.repository.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.data.connection.IterateStatistics;
import com.github.pathfinder.database.mapper.ValueMapper;
import com.github.pathfinder.database.repository.IPointConnectionRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointConnectionRepository implements IPointConnectionRepository {

    private static final String POINTS_IN_CHUNK_CONNECTION_QUERY = """
            CALL apoc.periodic.iterate(
            'MATCH (chunk:Chunk)-[:IN_CHUNK]->(first:Point)
               WHERE chunk.id = $chunkId
             RETURN chunk, first',
            'MATCH (chunk)-[:IN_CHUNK]->(second:Point)
               WHERE
               first <> second AND
               point.distance(first.location3d, second.location3d) <= $accuracyMeters AND
               NOT ((first)-[:CONNECTION]-(second))
             WITH first, second, point.distance(first.location3d, second.location3d) AS distanceMeters
             WITH first, second, distanceMeters,
                 ((second.passabilityCoefficient + first.passabilityCoefficient) / 2) * distanceMeters AS weight
             CREATE (first)-[:CONNECTION {distanceMeters: distanceMeters, weight: weight}]->(second)', {batchSize:  1,
                                                                                                      parallel:    true,
                                                                                                      concurrency: 3,
                                                                                                      retries:     10,
                                                                                                      params: {chunkId: $chunkId, accuracyMeters: $accuracyMeters}})
            YIELD batches, total, committedOperations, failedOperations, retries, batch, operations, timeTaken
            RETURN batches, total, committedOperations, failedOperations, retries, batch, operations, timeTaken
            """;

    private static final String CONNECT_CHUNK_BOARDERS_QUERY = """
            CALL apoc.periodic.iterate(
            'MATCH (firstChunk:Chunk)-[:IN_CHUNK]->(first:Point)
              WHERE firstChunk.id = $chunkId
             MATCH (secondChunk:Chunk)
                WHERE abs(firstChunk.min.x - secondChunk.max.x) <= $epsilon OR
                                    abs(firstChunk.min.y - secondChunk.max.y) <= $epsilon OR
                                    abs(firstChunk.max.x - secondChunk.min.x) <= $epsilon OR
                                    abs(firstChunk.max.y - secondChunk.min.y) <= $epsilon
            RETURN first, secondChunk',
            'MATCH (secondChunk)-[:IN_CHUNK]->(second:Point)
               WHERE first <> second AND
               point.distance(first.location3d, second.location3d) <= $accuracyMeters AND
               NOT ((first)-[:CONNECTION]-(second))
             WITH first, second, point.distance(first.location3d, second.location3d) AS distanceMeters
             WITH first, second, distanceMeters,
                 ((second.passabilityCoefficient + first.passabilityCoefficient) / 2) * distanceMeters AS weight
             CREATE (first)-[:CONNECTION {distanceMeters: distanceMeters, weight: weight}]->(second)', {batchSize:   1,
                                                                                                       parallel:    true,
                                                                                                       concurrency: 3,
                                                                                                       retries:     10,
                                                                                                       params:      {
                                                                                                                      chunkId:        $chunkId,
                                                                                                                      accuracyMeters: $accuracyMeters,
                                                                                                                      epsilon: $epsilon
                                                                                                                      }
                                                                                                                    })
            YIELD batches, total, committedOperations, failedOperations, retries, batch, operations, timeTaken
            RETURN batches, total, committedOperations, failedOperations, retries, batch, operations, timeTaken
            """;

    private final Neo4jClient client;
    private final ValueMapper mapper;

    @Override
    @Logged(value = {"chunkId", "accuracyMeters"})
    public Optional<IterateStatistics> connectPointsInChunk(Integer chunkId, Double accuracyMeters) {
        return iterate(POINTS_IN_CHUNK_CONNECTION_QUERY, Map.of(
                "chunkId", chunkId,
                "accuracyMeters", accuracyMeters
        ));
    }

    @Override
    @Logged(value = {"chunkId", "accuracyMeters"})
    public Optional<IterateStatistics> connectChunkBoarders(Integer chunkId, Double accuracyMeters) {
        return iterate(CONNECT_CHUNK_BOARDERS_QUERY, Map.of(
                "chunkId", chunkId,
                "accuracyMeters", accuracyMeters,
                "epsilon", 0.003
        ));
    }

    private Optional<IterateStatistics> iterate(String query, Map<String, Object> parameters) {
        return client
                .query(query)
                .bindAll(parameters)
                .fetchAs(IterateStatistics.class)
                .mappedBy((typeSystem, fetched) -> mapper.map(typeSystem, IterateStatistics.class, fetched))
                .one();
    }

}
