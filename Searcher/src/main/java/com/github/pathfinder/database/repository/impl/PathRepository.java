package com.github.pathfinder.database.repository.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.database.mapper.ValueMapper;
import com.github.pathfinder.database.repository.IPathRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PathRepository implements IPathRepository {

    private static final String A_STAR_QUERY = """
            MATCH (source:Point {id: $sourceId})
            WITH source
            MATCH (target:Point {id: $targetId})
            CALL gds.shortestPath.astar.stream($graphName, {
              sourceNode:        source,
              targetNode:        target,
              concurrency:       1,
              logProgress:       false,
              latitudeProperty:  'latitude',
              longitudeProperty: 'longitude',
              relationshipWeightProperty: 'weight'
            })
            YIELD path, totalCost
            RETURN nodes(path) as path, totalCost
            """;

    private final Neo4jClient client;
    private final ValueMapper mapper;

    @Override
    @Logged(value = {"graphName", "sourceId", "targetId"})
    public Optional<AStarResult> aStar(String graphName, UUID sourceId, UUID targetId) {
        return client
                .query(A_STAR_QUERY)
                .bindAll(Map.of(
                        "sourceId", sourceId.toString(),
                        "targetId", targetId.toString(),
                        "graphName", graphName
                ))
                .fetchAs(AStarResult.class)
                .mappedBy((typeSystem, fetched) -> mapper.map(typeSystem, AStarResult.class, fetched))
                .one();
    }

}
