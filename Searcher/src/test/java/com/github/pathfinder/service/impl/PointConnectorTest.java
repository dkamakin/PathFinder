package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import com.github.pathfinder.database.repository.impl.PointConnectionRepository;
import com.github.pathfinder.service.IChunkUpdaterService;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
    IChunkUpdaterService chunkUpdaterService;

    @Autowired
    Neo4jTestTemplate testTemplate;

    @Autowired
    ChunkGetterService chunkGetterService;

    static Stream<Arguments> argumentsForBoarderCases() {
        return Stream.of(
                // west
                Arguments.of(
                        new Coordinate(45.160227, 19.823118),
                        new Coordinate(45.160238, 19.823375)
                ),
                Arguments.of(
                        new Coordinate(45.174126, 19.823134),
                        new Coordinate(45.174137, 19.823343)
                ),
                // north
                Arguments.of(
                        new Coordinate(45.183341, 19.835048),
                        new Coordinate(45.183205, 19.835054)
                ),
                Arguments.of(
                        new Coordinate(45.183315, 19.884015),
                        new Coordinate(45.183201, 19.883988)
                ),
                // east
                Arguments.of(
                        new Coordinate(45.176743, 19.890785),
                        new Coordinate(45.176743, 19.890677)
                ),
                Arguments.of(
                        new Coordinate(45.171006, 19.890779),
                        new Coordinate(45.170995, 19.890677)
                ),
                // south
                Arguments.of(
                        new Coordinate(45.138416, 19.826599),
                        new Coordinate(45.138511, 19.826578)
                ),
                Arguments.of(
                        new Coordinate(45.138405, 19.880517),
                        new Coordinate(45.138514, 19.880485)
                )
        );
    }

    ChunkNode chunk(int id, List<PointNode> pointNodes) {
        return PointFixtures.randomChunkNodeBuilder().points(pointNodes).id(id).build();
    }

    @Test
    void createConnections_ChunksFound_MarkChunksAsConnected() {
        chunkUpdaterService.save(chunk(1, List.of(PointFixtures.randomPointNodeBuilder()
                                                          .passabilityCoefficient(2D)
                                                          .location(44.82744118518296, 20.419457053285115, 1D)
                                                          .build())));
        chunkUpdaterService.save(chunk(2, List.of(PointFixtures.randomPointNodeBuilder()
                                                          .passabilityCoefficient(1D)
                                                          .location(44.827410791880155, 20.419468330585666, 1D)
                                                          .build())));

        var chunkId = 1;
        var ids     = List.of(1);

        assertThat(chunkGetterService.simple(ids))
                .hasSameSizeAs(ids)
                .noneMatch(SimpleChunk::connected);

        target.createConnections(chunkId);

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

        chunkUpdaterService.save(chunk(chunkId, List.of(firstPoint, secondPoint, tooFarAwayPoint.add(randomRelation))));

        target.createConnections(chunkId);

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
    void createConnections_PointsAreNotConnected_ConnectOnlyInOneWay() {
        var chunkId = 1;

        chunkUpdaterService.save(chunk(chunkId, List.of(
                PointFixtures.pointNodeBuilder()
                        .passabilityCoefficient(1D)
                        .location(44.827410791880155, 20.419468330585666, 1D).build(),
                PointFixtures.pointNodeBuilder()
                        .passabilityCoefficient(2D)
                        .location(44.82744118518296, 20.419457053285115, 1D).build(),
                PointFixtures.pointNodeBuilder()
                        .passabilityCoefficient(2D)
                        .location(44.82744118518296, 20.419457053285116, 1D).build(),
                PointFixtures.pointNodeBuilder()
                        .passabilityCoefficient(2D)
                        .location(44.82744118518296, 20.419457053285117, 1D).build(),
                PointFixtures.pointNodeBuilder()
                        .passabilityCoefficient(2D)
                        .location(44.82744118518296, 20.419457053285118, 1D).build(),
                PointFixtures.pointNodeBuilder()
                        .passabilityCoefficient(2D)
                        .location(44.82744118518296, 20.419457053285119, 1D).build(),
                PointFixtures.pointNodeBuilder()
                        .passabilityCoefficient(2D)
                        .location(44.82744118518296, 20.419457053285120, 1D).build()
        )));

        target.createConnections(chunkId);

        var actual = testTemplate.allPointNodes();

        assertThat(actual)
                .hasSize(7)
                .anyMatch(node -> node.getRelations().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("argumentsForBoarderCases")
    void createConnections_PointIsOnTheBoarderButNotInChunk_ConnectAnyway(
            Coordinate boarderPointCoordinate,
            Coordinate chunkPointCoordinate) {
        var chunkPoint = PointFixtures.pointNodeBuilder()
                .location(chunkPointCoordinate.latitude(), chunkPointCoordinate.longitude(), 1D)
                .passabilityCoefficient(1D)
                .build();
        var boarderPoint = PointFixtures.pointNodeBuilder()
                .location(boarderPointCoordinate.latitude(), boarderPointCoordinate.longitude(), 1D)
                .passabilityCoefficient(2D)
                .build();

        var targetChunk = chunkUpdaterService.save(ChunkNode.builder()
                                                           .min(45.138461, 19.823284)
                                                           .max(45.183247, 19.890747)
                                                           .id(1)
                                                           .points(List.of(chunkPoint))
                                                           .build());
        chunkUpdaterService.save(PointFixtures.randomChunkNodeBuilder()
                                         .id(2)
                                         .points(List.of(boarderPoint))
                                         .build());

        assertThat(testTemplate.allPointNodes())
                .hasSize(2)
                .allMatch(point -> point.getRelations().isEmpty());

        target.createConnections(targetChunk.getId());

        assertThat(testTemplate.allPointNodes())
                .hasSize(2)
                .anySatisfy(node -> assertThat(node.getRelations()).hasSize(1));
    }

}
