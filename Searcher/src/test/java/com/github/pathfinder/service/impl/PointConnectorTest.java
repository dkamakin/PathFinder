package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import com.github.pathfinder.database.repository.impl.PointConnectionRepository;
import com.github.pathfinder.service.IPointService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

@SearcherNeo4jTest
@Import({PointConnector.class, ChunkGetterService.class, PointConnectionRepository.class})
class PointConnectorTest {

    @Autowired
    PointConnector target;

    @Autowired
    IPointService pointService;

    @Autowired
    Neo4jTestTemplate testTemplate;

    @Autowired
    ChunkGetterService chunkGetterService;

    @Test
    void createConnections_ChunksNotFound_NoException() {
        pointService.saveAll(1, List.of(PointFixtures.randomPointNodeBuilder().passabilityCoefficient(2D)
                                                .location(44.82744118518296, 20.419457053285115, 1D).build()));
        pointService.saveAll(2, List.of(PointFixtures.randomPointNodeBuilder().passabilityCoefficient(1D)
                                                .location(44.827410791880155, 20.419468330585666, 1D).build()));

        var ids            = List.of(1, 2);
        var notExistingIds = List.of(134, 43);

        assertThat(chunkGetterService.simple(notExistingIds)).isEmpty();

        assertThat(chunkGetterService.simple(ids)).hasSameSizeAs(ids).noneMatch(SimpleChunk::connected);

        target.createConnections(notExistingIds);

        assertThat(chunkGetterService.simple(notExistingIds)).isEmpty();

        assertThat(chunkGetterService.simple(ids)).hasSameSizeAs(ids).noneMatch(SimpleChunk::connected);
    }

    @Test
    void createConnections_ChunksFound_MarkChunksAsConnected() {
        pointService.saveAll(1, List.of(PointFixtures.randomPointNodeBuilder()
                                                .passabilityCoefficient(2D)
                                                .location(44.82744118518296, 20.419457053285115, 1D).build()));
        pointService.saveAll(2, List.of(PointFixtures.randomPointNodeBuilder()
                                                .passabilityCoefficient(1D)
                                                .location(44.827410791880155, 20.419468330585666, 1D).build()));

        var ids = List.of(1, 2);

        assertThat(chunkGetterService.simple(ids))
                .hasSameSizeAs(ids)
                .noneMatch(SimpleChunk::connected);

        target.createConnections(ids);

        assertThat(chunkGetterService.simple(ids))
                .hasSameSizeAs(ids)
                .allMatch(SimpleChunk::connected);
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

        pointService.saveAll(chunkId, List.of(firstPoint, secondPoint, tooFarAwayPoint.add(randomRelation)));
        target.createConnections(List.of(chunkId));

        var actual = testTemplate.allPointNodes();

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

    @Test
    void createConnections_NegativeWeightFound_DoNotConnectWithNegativeValue() {
        var firstPoint = PointFixtures.randomPointNodeBuilder()
                .passabilityCoefficient(1D)
                .location(44.827410791880155, 20.419468330585666, 1D).build();
        var secondPoint = PointFixtures.randomPointNodeBuilder()
                .passabilityCoefficient(-2D)
                .location(44.82744118518296, 20.419457053285115, 1D).build();
        var chunkId = 1;

        pointService.saveAll(chunkId, List.of(firstPoint, secondPoint));
        target.createConnections(List.of(chunkId));

        var actual = testTemplate.allPointNodes();

        assertThat(actual)
                .hasSize(2)
                .anySatisfy(connected -> assertThat(connected)
                        .isEqualTo(firstPoint)
                        .satisfies(node -> assertThat(node.getRelations())
                                .isEmpty()));
    }

}
