package com.github.pathfinder.database.repository.impl;

import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.database.mapper.ValueMapper;
import com.github.pathfinder.database.repository.IPathRepository;
import com.github.pathfinder.exception.PathNotFoundException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.TypeSystem;
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
              relationshipWeightProperty: 'distance'
            })
            YIELD path, totalCost
            RETURN nodes(path) as path, totalCost
            """;

    private final Neo4jClient client;
    private final ValueMapper mapper;

    @Override
    public AStarResult aStar(String graphName, UUID sourceId, UUID targetId) {
        return client
                .query(A_STAR_QUERY)
                .bindAll(Map.of(
                        "sourceId", sourceId.toString(),
                        "targetId", targetId.toString(),
                        "graphName", graphName
                ))
                .fetchAs(AStarResult.class)
                .mappedBy(this::aStarResult)
                .one()
                .orElseThrow(PathNotFoundException::new);
    }

    private AStarResult aStarResult(TypeSystem typeSystem, Record fetched) {
        return mapper.map(typeSystem, AStarResult.class, fetched);
    }

}
