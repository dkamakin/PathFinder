package com.github.pathfinder.database.repository.impl;

import com.github.pathfinder.core.exception.InternalServerException;
import com.github.pathfinder.data.projection.ProjectionStatistics;
import com.github.pathfinder.database.mapper.ValueMapper;
import com.github.pathfinder.database.repository.IProjectionRepository;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectionRepository implements IProjectionRepository {

    private final Neo4jClient client;
    private final ValueMapper mapper;

    @Override
    public Collection<String> all() {
        return client.query("""
                                    CALL gds.graph.list() YIELD graphName RETURN graphName
                                    """)
                .fetchAs(String.class)
                .all();
    }

    @Override
    public Collection<String> deleteAll(Collection<String> graphNames) {
        return client.query("""
                                    UNWIND $graphNames AS name
                                     CALL gds.graph.drop(
                                     name,
                                     false
                                     )
                                     YIELD graphName
                                     RETURN graphName
                                                             """)
                .bindAll(Map.of("graphNames", graphNames))
                .fetchAs(String.class)
                .all();
    }

    @Override
    public ProjectionStatistics createProjection(String graphName) {
        return client.query("""
                                    CALL gds.graph.project(
                                    $graphName,
                                    'Point',
                                    {CONNECTION: {orientation: 'UNDIRECTED'}},
                                    {
                                      nodeProperties:         ['latitude', 'longitude'],
                                      relationshipProperties: 'weight'
                                    })
                                    YIELD nodeCount, relationshipCount
                                    RETURN nodeCount, relationshipCount
                                                            """)
                .bindAll(Map.of("graphName", graphName))
                .fetchAs(ProjectionStatistics.class)
                .mappedBy(((typeSystem, fetched) -> mapper.map(typeSystem, ProjectionStatistics.class, fetched)))
                .one()
                .orElseThrow(() -> new InternalServerException("Failed to create a projection: " + graphName));
    }

    @Override
    public boolean exists(String graphName) {
        return client.query("""
                                    CALL gds.graph.exists($graphName)
                                    YIELD exists
                                    RETURN exists
                                    """)
                .bindAll(Map.of("graphName", graphName))
                .fetchAs(boolean.class)
                .one()
                .orElse(false);
    }

}
