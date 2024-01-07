package com.github.pathfinder.configuration;

import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.service.IPointService;
import com.github.pathfinder.service.IProjectionService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@RequiredArgsConstructor
public class Neo4jTestTemplate {

    private final Neo4jTemplate       neo4jTemplate;
    private final TestNeo4jRepository testRepository;
    private final IProjectionService  projectionService;
    private final IPointService pointService;

    @Transactional
    public void cleanDatabase() {
        Set.of(PointNode.class, PointRelation.class, ChunkNode.class).forEach(neo4jTemplate::deleteAll);
        projectionService.deleteAll();
    }

    public List<PointNode> saveAll(List<PointNode> nodes) {
        return pointService.saveAll(1, nodes);
    }

    @ReadTransactional
    public List<PointNode> allNodes() {
        return testRepository.findAll();
    }

}
