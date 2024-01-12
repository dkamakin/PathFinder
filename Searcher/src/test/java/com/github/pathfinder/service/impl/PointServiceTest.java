package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.service.IPointService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

@SearcherNeo4jTest
class PointServiceTest {

    @Autowired
    IPointService target;

    @Autowired
    ChunkUpdaterService chunkService;

    @Autowired
    Neo4jTestTemplate testTemplate;

    @Test
    void saveAll_PointsAreConnected_StoreConnection() {
        var sourcePoint = PointFixtures.randomPointNode();
        var targetPoint = PointFixtures.randomPointNode();
        var connection  = new PointRelation(12D, 13D, targetPoint);
        var secondPoint = PointFixtures.randomPointNode();
        var id          = 1234;
        var actual      = target.saveAll(id, List.of(sourcePoint.add(connection), secondPoint));
        var noiseChunk  = chunkService.save(ChunkNode.builder().id(id + 1).build());

        System.out.println("Noise chunk: " + noiseChunk);

        assertThat(actual)
                .hasSize(2)
                .anySatisfy(first -> assertThat(first)
                        .isEqualTo(sourcePoint)
                        .matches(saved -> StringUtils.isNotEmpty(saved.getInternalId()))
                        .satisfies(saved -> assertThat(saved.getRelations())
                                .hasSize(1)
                                .first()
                                .matches(x -> x.getTarget().equals(targetPoint))
                                .isEqualTo(connection)))
                .anySatisfy(second -> assertThat(second)
                        .matches(saved -> StringUtils.isNotEmpty(saved.getInternalId()))
                        .isEqualTo(secondPoint));

        assertThat(testTemplate.chunkNodes(List.of(id)))
                .hasSize(1)
                .first()
                .matches(chunk -> chunk.getId() == id)
                .satisfies(chunk -> assertThat(chunk.getPointRelations())
                        .hasSameSizeAs(actual)
                        .allMatch(relation -> actual.contains(relation.getTarget())));
    }

}