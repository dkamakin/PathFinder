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
import static org.assertj.core.api.Assertions.withinPercentage;

@SearcherNeo4jTest
class PointServiceTest {

    @Autowired
    IPointService target;

    @Autowired
    Neo4jTestTemplate testTemplate;

    @Autowired
    ChunkService chunkService;

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

        assertThat(chunkService.extendedChunks(List.of(id)))
                .hasSize(1)
                .first()
                .matches(chunk -> chunk.getId() == id)
                .satisfies(chunk -> assertThat(chunk.getPointRelations())
                        .hasSameSizeAs(actual)
                        .allMatch(relation -> actual.contains(relation.getTarget())));
    }

    @Test
    void createConnections_ChunksNotFound_NoException() {
        target.saveAll(1, List.of(PointFixtures.randomPointNodeBuilder().passabilityCoefficient(2D)
                                          .location(44.82744118518296, 20.419457053285115, 1D).build()));
        target.saveAll(2, List.of(PointFixtures.randomPointNodeBuilder().passabilityCoefficient(1D)
                                          .location(44.827410791880155, 20.419468330585666, 1D).build()));

        var ids            = List.of(1, 2);
        var notExistingIds = List.of(134, 43);

        assertThat(chunkService.chunks(notExistingIds)).isEmpty();

        assertThat(chunkService.chunks(ids)).hasSameSizeAs(ids).noneMatch(ChunkNode::isConnected);

        target.createConnections(notExistingIds);

        assertThat(chunkService.chunks(notExistingIds)).isEmpty();

        assertThat(chunkService.chunks(ids)).hasSameSizeAs(ids).noneMatch(ChunkNode::isConnected);
    }

    @Test
    void createConnections_ChunksFound_MarkChunksAsConnected() {
        target.saveAll(1, List.of(PointFixtures.randomPointNodeBuilder()
                                          .passabilityCoefficient(2D)
                                          .location(44.82744118518296, 20.419457053285115, 1D).build()));
        target.saveAll(2, List.of(PointFixtures.randomPointNodeBuilder()
                                          .passabilityCoefficient(1D)
                                          .location(44.827410791880155, 20.419468330585666, 1D).build()));

        var ids = List.of(1, 2);

        assertThat(chunkService.chunks(ids))
                .hasSameSizeAs(ids)
                .noneMatch(ChunkNode::isConnected);

        target.createConnections(ids);

        assertThat(chunkService.chunks(ids))
                .hasSameSizeAs(ids)
                .allMatch(ChunkNode::isConnected);
    }

    @Test
    void createConnections_PointsAreNotConnected_ConnectOnlyWithinAccuracy() {
        var firstPoint = PointFixtures.randomPointNodeBuilder()
                .passabilityCoefficient(1D)
                .location(44.827410791880155, 20.419468330585666, 1D).build();
        var secondPoint = PointFixtures.randomPointNodeBuilder()
                .passabilityCoefficient(2D)
                .location(44.82744118518296, 20.419457053285115, 1D).build();
        var tooFarAwayPoint = PointFixtures.randomPointNodeBuilder()
                .passabilityCoefficient(3D)
                .location(44.82755949185502, 20.413331663727266, 1D).build();
        var randomPoint    = PointFixtures.randomPointNode();
        var randomRelation = new PointRelation(1D, 1D, randomPoint);
        var chunkId        = 1;

        target.saveAll(chunkId, List.of(firstPoint, secondPoint, tooFarAwayPoint.add(randomRelation)));
        target.createConnections(List.of(chunkId));

        var actual = testTemplate.allNodes();

        assertThat(actual)
                .hasSize(4)
                .anySatisfy(random -> assertThat(random)
                        .isEqualTo(randomPoint)
                        .matches(x -> x.getRelations().isEmpty()))
                .anySatisfy(farPoint -> assertThat(farPoint)
                        .isEqualTo(tooFarAwayPoint)
                        .satisfies(x -> assertThat(x.getRelations())
                                .hasSize(1)
                                .contains(randomRelation)))
                .anySatisfy(connected -> assertThat(connected)
                        .isEqualTo(firstPoint)
                        .satisfies(node -> assertThat(node.getRelations())
                                .hasSize(1)
                                .first()
                                .satisfies(relation -> assertThat(relation.getWeight())
                                        .isCloseTo(5.247841282633755, withinPercentage(0.99)))
                                .satisfies(relation -> assertThat(relation.getDistanceMeters())
                                        .isCloseTo(3.49856085508917, withinPercentage(0.99)))
                                .matches(relation -> relation.getTarget().equals(secondPoint))));
    }

}