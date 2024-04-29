package com.github.pathfinder.database.repository.impl;

import java.util.Map;
import java.util.Optional;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.data.connection.IterateStatistics;
import com.github.pathfinder.database.mapper.ValueMapper;
import com.github.pathfinder.database.repository.IPointConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointConnectionRepository implements IPointConnectionRepository {

    private static final String POINTS_IN_CHUNK_CONNECTION_QUERY = """
            CALL apoc.periodic.iterate(
            '
            MATCH (chunk:Chunk)
                WHERE chunk.id = $chunkId
            WITH chunk
            MATCH (point:Point)
                WHERE point.withinBBox(
                point.location2d,
                point({latitude: chunk.min.y - $epsilon, longitude: chunk.min.x - $epsilon}),
                point({latitude: chunk.max.y + $epsilon, longitude: chunk.max.x + $epsilon})
            )
            RETURN chunk, point AS first',
            '
            MATCH (chunk)-[:IN_CHUNK]->(second:Point)
                WHERE first <> second AND
                NOT (first)-[:CONNECTION]-(second) AND
                point.distance(first.location3d, second.location3d) <= $accuracyMeters
            WITH first, second, point.distance(first.location3d, second.location3d) AS distanceMeters
            WITH first, second, distanceMeters,
                ((second.passabilityCoefficient + first.passabilityCoefficient) / 2) * distanceMeters AS weight
            CREATE (first)-[:CONNECTION {distanceMeters: distanceMeters, weight: weight}]->(second)',
            {
            batchSize:   1,
            retries:     10,
            params: {
            chunkId:        $chunkId,
            accuracyMeters: $accuracyMeters,
            epsilon:        $epsilon
            }})
            YIELD  batches, total, committedOperations, failedOperations, retries, batch, operations, timeTaken
            RETURN batches, total, committedOperations, failedOperations, retries, batch, operations, timeTaken
            """;

    private final Neo4jClient client;
    private final ValueMapper mapper;

    @Override
    @Logged(value = {"chunkId", "accuracyMeters"})
    public Optional<IterateStatistics> connectChunkPoints(Integer chunkId, Double accuracyMeters) {
        return iterate(POINTS_IN_CHUNK_CONNECTION_QUERY, Map.of(
                "chunkId", chunkId,
                "accuracyMeters", accuracyMeters,
                "epsilon", 0.002
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
