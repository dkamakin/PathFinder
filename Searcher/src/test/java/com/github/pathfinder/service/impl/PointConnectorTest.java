package com.github.pathfinder.service.impl;

import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import com.github.pathfinder.database.repository.impl.PointConnectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@SearcherNeo4jTest
@Import({PointConnector.class, ChunkGetterService.class, PointConnectionRepository.class})
class PointConnectorTest {

    static final Coordinate WEST_BOX_MIN = new Coordinate(45.124292, 19.781742);
    static final Coordinate WEST_BOX_MAX = new Coordinate(45.188328, 19.822083);

    static final Coordinate NORTH_BOX_MIN = new Coordinate(45.183005, 19.811268);
    static final Coordinate NORTH_BOX_MAX = new Coordinate(45.194014, 19.976234);

    static final Coordinate EAST_BOX_MIN = new Coordinate(45.131559, 19.888515);
    static final Coordinate EAST_BOX_MAX = new Coordinate(45.190506, 19.939842);

    static final Coordinate SOUTH_BOX_MIN = new Coordinate(45.123081, 19.802856);
    static final Coordinate SOUTH_BOX_MAX = new Coordinate(45.139672, 19.941387);

    static final Coordinate NORTH_WEST_BOX_MIN = new Coordinate(45.183791, 19.801397);
    static final Coordinate NORTH_WEST_BOX_MAX = new Coordinate(45.195617, 19.823327);

    static final Coordinate NORTH_EAST_BOX_MIN = new Coordinate(45.183307, 19.891015);
    static final Coordinate NORTH_EAST_BOX_MAX = new Coordinate(45.185712, 19.895296);

    static final Coordinate SOUTH_EAST_BOX_MIN = new Coordinate(45.130620, 19.890490);
    static final Coordinate SOUTH_EAST_BOX_MAX = new Coordinate(45.138734, 19.907870);

    static final Coordinate SOUTH_WEST_BOX_MIN = new Coordinate(45.134147, 19.819465);
    static final Coordinate SOUTH_WEST_BOX_MAX = new Coordinate(45.138719, 19.823670);

    @Autowired
    PointConnector target;

    @Autowired
    ChunkUpdaterService chunkUpdaterService;

    @Autowired
    Neo4jTestTemplate testTemplate;

    @Autowired
    ChunkGetterService chunkGetterService;

    static Stream<Arguments> argumentsForBoarderCases() {
        return Stream.of(
                // west
                Arguments.of(
                        new Coordinate(45.160227, 19.823118),
                        new Coordinate(45.160238, 19.823375),
                        WEST_BOX_MIN,
                        WEST_BOX_MAX
                ),
                Arguments.of(
                        new Coordinate(45.174126, 19.823134),
                        new Coordinate(45.174137, 19.823343),
                        WEST_BOX_MIN,
                        WEST_BOX_MAX
                ),
                // north
                Arguments.of(
                        new Coordinate(45.183341, 19.835048),
                        new Coordinate(45.183205, 19.835054),
                        NORTH_BOX_MIN,
                        NORTH_BOX_MAX
                ),
                Arguments.of(
                        new Coordinate(45.183315, 19.884015),
                        new Coordinate(45.183201, 19.883988),
                        NORTH_BOX_MIN,
                        NORTH_BOX_MAX
                ),
                // east
                Arguments.of(
                        new Coordinate(45.176743, 19.890785),
                        new Coordinate(45.176743, 19.890677),
                        EAST_BOX_MIN,
                        EAST_BOX_MAX
                ),
                Arguments.of(
                        new Coordinate(45.171006, 19.890779),
                        new Coordinate(45.170995, 19.890677),
                        EAST_BOX_MIN,
                        EAST_BOX_MAX
                ),
                // south
                Arguments.of(
                        new Coordinate(45.138416, 19.826599),
                        new Coordinate(45.138511, 19.826578),
                        SOUTH_BOX_MIN,
                        SOUTH_BOX_MAX
                ),
                Arguments.of(
                        new Coordinate(45.138405, 19.880517),
                        new Coordinate(45.138514, 19.880485),
                        SOUTH_BOX_MIN,
                        SOUTH_BOX_MAX
                ),
                // north-west
                Arguments.of(
                        new Coordinate(45.183345, 19.823134),
                        new Coordinate(45.183145, 19.823413),
                        NORTH_WEST_BOX_MIN,
                        NORTH_WEST_BOX_MAX
                ),
                // north-east
                Arguments.of(
                        new Coordinate(45.183288, 19.890876),
                        new Coordinate(45.183179, 19.890629),
                        NORTH_EAST_BOX_MIN,
                        NORTH_EAST_BOX_MAX
                ),
                // south-east
                Arguments.of(
                        new Coordinate(45.138363, 19.890838),
                        new Coordinate(45.138533, 19.890592),
                        SOUTH_EAST_BOX_MIN,
                        SOUTH_EAST_BOX_MAX
                ),
                // south-west
                Arguments.of(
                        new Coordinate(45.138393, 19.823171),
                        new Coordinate(45.138556, 19.823391),
                        SOUTH_WEST_BOX_MIN,
                        SOUTH_WEST_BOX_MAX
                )
        );
    }

    ChunkNode chunk(int id, List<PointNode> pointNodes) {
        return ChunkNode.builder()
                .min(44.824221, 20.412083)
                .max(44.834448, 20.428648)
                .points(pointNodes)
                .id(id)
                .build();
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
                        .location(44.82744118518296, 20.419457053285115, 1D).build()
        )));

        target.createConnections(chunkId);

        var actual = testTemplate.allPointNodes();

        assertThat(actual)
                .hasSize(2)
                .anyMatch(node -> node.getRelations().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("argumentsForBoarderCases")
    void createConnections_PointIsOnTheBoarderButNotInChunk_ConnectAnyway(
            Coordinate boarderPointCoordinate,
            Coordinate chunkPointCoordinate,
            Coordinate boarderChunkMin,
            Coordinate boarderChunkMax) {
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
        chunkUpdaterService.save(ChunkNode.builder()
                                         .min(boarderChunkMin)
                                         .max(boarderChunkMax)
                                         .id(2)
                                         .points(List.of(boarderPoint))
                                         .build());
        List.of(3, 5, 99).forEach(noiseChunkId -> PointFixtures.randomChunkNodeBuilder()
                .id(noiseChunkId)
                .points(List.of(PointFixtures.randomPointNode()))
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
