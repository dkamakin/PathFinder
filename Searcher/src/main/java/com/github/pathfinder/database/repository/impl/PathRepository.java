package com.github.pathfinder.database.repository.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.database.mapper.ValueMapper;
import com.github.pathfinder.database.repository.IPathRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PathRepository implements IPathRepository {

    private static final String A_STAR_QUERY = """
            MATCH (source:Point)
            WITH source, point.distance(source.location2d, point({latitude: $sourceLat, longitude: $sourceLon})) AS distanceSource
              ORDER BY distanceSource
              LIMIT 1
            MATCH (target:Point)
            WITH source, target, point.distance(target.location2d, point({latitude: $targetLat, longitude: $targetLon})) AS distanceTarget
              ORDER BY distanceTarget
              LIMIT 1
            CALL apoc.algo.aStarConfig(source, target, 'CONNECTION', {weight: 'weight', pointPropName: 'location2d', default: 1})
            YIELD path, weight
            RETURN nodes(path) as path, weight, reduce(meters = 0, relation in relationships(path) | meters + relation.distanceMeters) as meters
            """;

    private final Neo4jClient client;
    private final ValueMapper mapper;

    @Override
    @Logged(value = {"source", "target"}, ignoreReturnValue = false)
    public Optional<AStarResult> aStar(Coordinate source, Coordinate target) {
        return client
                .query(A_STAR_QUERY)
                .bindAll(Map.of(
                        "sourceLat", source.latitude(),
                        "sourceLon", source.longitude(),
                        "targetLat", target.latitude(),
                        "targetLon", target.longitude()
                ))
                .fetchAs(AStarResult.class)
                .mappedBy((typeSystem, fetched) -> mapper.map(typeSystem, AStarResult.class, fetched))
                .one();
    }

}
