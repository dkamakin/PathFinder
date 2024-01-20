package com.github.pathfinder.database.repository.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.data.connection.PointConnectionStatistics;
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

    private static final String CONNECTION_QUERY = """
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

    private final Neo4jClient client;
    private final ValueMapper mapper;

    @Override
    @Logged(value = {"chunkId", "accuracyMeters"})
    public Optional<PointConnectionStatistics> createConnections(Integer chunkId, Double accuracyMeters) {
        return client
                .query(CONNECTION_QUERY)
                .bindAll(Map.of(
                        "chunkId", chunkId,
                        "accuracyMeters", accuracyMeters
                ))
                .fetchAs(PointConnectionStatistics.class)
                .mappedBy((typeSystem, fetched) -> mapper.map(typeSystem, PointConnectionStatistics.class, fetched))
                .one();
    }

}
