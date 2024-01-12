package com.github.pathfinder.database.repository.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.data.connection.PointConnectionStatistics;
import com.github.pathfinder.database.mapper.ValueMapper;
import com.github.pathfinder.database.repository.IPointConnectionRepository;
import java.util.List;
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
            'MATCH (chunk:Chunk)-[:IN_CHUNK]-(first:Point) WHERE chunk.id IN $chunkIds RETURN first',
            'MATCH (second:Point) WHERE elementId(first) <> elementId(second) WITH first, second, point.distance(first.location3d, second.location3d) AS distanceMeters WHERE distanceMeters <= $accuracyMeters WITH first, second, distanceMeters, ((second.passabilityCoefficient + first.passabilityCoefficient) / 2) * distanceMeters AS weight MERGE (first)-[:CONNECTION {distanceMeters: distanceMeters, weight: weight}]->(second)',
            {batchSize: 1, parallel: true, concurrency: 4, params: {chunkIds: $chunkIds, accuracyMeters: $accuracyMeters}})
            YIELD batches, total, committedOperations, failedOperations, retries
            RETURN batches, total, committedOperations, failedOperations, retries
            """;

    private final Neo4jClient client;
    private final ValueMapper mapper;

    @Override
    @Logged(value = {"chunkIds", "accuracyMeters"})
    public Optional<PointConnectionStatistics> createConnections(List<Integer> chunkIds, Double accuracyMeters) {
        return client
                .query(CONNECTION_QUERY)
                .bindAll(Map.of(
                        "chunkIds", chunkIds,
                        "accuracyMeters", accuracyMeters
                ))
                .fetchAs(PointConnectionStatistics.class)
                .mappedBy((typeSystem, fetched) -> mapper.map(typeSystem, PointConnectionStatistics.class, fetched))
                .one();
    }

}
