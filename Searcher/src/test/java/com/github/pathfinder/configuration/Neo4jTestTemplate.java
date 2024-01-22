package com.github.pathfinder.configuration;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.repository.TestPointNodeRepository;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.service.IChunkUpdaterService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@RequiredArgsConstructor
public class Neo4jTestTemplate {

    private final Neo4jTemplate           neo4jTemplate;
    private final TestPointNodeRepository pointRepository;
    private final IChunkUpdaterService    chunkUpdaterService;

    @Transactional
    public void cleanDatabase() {
        Set.of(PointNode.class, PointRelation.class, ChunkNode.class).forEach(neo4jTemplate::deleteAll);
    }

    public void save(PointNode node) {
        saveAll(List.of(node));
    }

    public void saveAll(List<PointNode> nodes) {
        chunkUpdaterService.save(PointFixtures.randomChunkNodeBuilder().points(nodes).id(1).build());
    }

    @ReadTransactional
    public List<PointNode> allPointNodes() {
        return pointRepository.findAll();
    }

}
