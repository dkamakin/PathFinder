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
             MERGE (first)-[:CONNECTION {distanceMeters: distanceMeters, weight: weight}]->(second)', {batchSize:  1,
                                                                                                      parallel:    true,
                                                                                                      concurrency: 4,
                                                                                                      retries:     10,
                                                                                                      params: {chunkId: $chunkId, accuracyMeters: $accuracyMeters}})
            YIELD batches, total, committedOperations, failedOperations, retries, batch, operations
            RETURN batches, total, committedOperations, failedOperations, retries, batch, operations
            """;

    private static final String CONNECT_CHUNK_BOARDERS_QUERY = """
            CALL apoc.periodic.iterate(
            'MATCH (chunk:Chunk)
              WHERE chunk.id = $chunkId
            MATCH (first:Point)
              WHERE (first.location2d.x >= chunk.min.x AND first.location2d.x <= chunk.max.x AND abs(first.location2d.y - chunk.min.y) < 0.001743) OR
              (first.location2d.x >= chunk.min.x AND first.location2d.x <= chunk.max.x AND abs(first.location2d.y - chunk.max.y) < 0.001743) OR
              (first.location2d.y >= chunk.min.y AND first.location2d.y <= chunk.max.y AND abs(first.location2d.x - chunk.min.x) < 0.001743) OR
              ((first.location2d.y >= chunk.min.y AND first.location2d.y <= chunk.max.y AND abs(first.location2d.x - chunk.max.x) < 0.001743))
            RETURN first, chunk',
            'MATCH (second:Point)
              WHERE first <> second AND ((second.location2d.x >= chunk.min.x AND second.location2d.x <= chunk.max.x AND abs(second.location2d.y - chunk.min.y) < 0.001743) OR
              (second.location2d.x >= chunk.min.x AND second.location2d.x <=chunk.max.x AND abs(second.location2d.y - chunk.max.y) < 0.001743) OR
              (second.location2d.y >= chunk.min.y AND second.location2d.y <=chunk.max.y AND abs(second.location2d.x - chunk.min.x) < 0.001743) OR
              ((second.location2d.y >= chunk.min.y AND second.location2d.y <=chunk.max.y AND abs(second.location2d.x - chunk.max.x) < 0.001743))) AND
               point.distance(first.location3d, second.location3d) <= $accuracyMeters AND
               NOT ((first)-[:CONNECTION]-(second))
             WITH first, second, point.distance(first.location3d, second.location3d) AS distanceMeters
             WITH first, second, distanceMeters,
                 ((second.passabilityCoefficient + first.passabilityCoefficient) / 2) * distanceMeters AS weight
             MERGE (first)-[:CONNECTION {distanceMeters: distanceMeters, weight: weight}]->(second)', {batchSize:   1,
                                                                                                       parallel:    true,
                                                                                                       concurrency: 4,
                                                                                                       retries:     10,
                                                                                                       params:      {
                                                                                                                      chunkId:        $chunkId,
                                                                                                                      accuracyMeters: $accuracyMeters}})
            YIELD batches, total, committedOperations, failedOperations, retries, batch, operations
            RETURN batches, total, committedOperations, failedOperations, retries, batch, operations
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
                "accuracyMeters", accuracyMeters
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
