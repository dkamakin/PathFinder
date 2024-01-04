package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.CoordinateConfiguration;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.service.IPointService;
import com.github.pathfinder.service.IProjectionService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@SearcherNeo4jTest
@Import({PointService.class, CoordinateConfiguration.class, ProjectionService.class})
class PointServiceTest {

    @Autowired
    IPointService target;

    @Autowired
    Neo4jTestTemplate testTemplate;

    @Autowired
    IProjectionService projectionService;

    @Test
    void saveAll_PointsAreConnected_StoreConnection() {
        var sourcePoint = PointFixtures.randomPointNode();
        var targetPoint = PointFixtures.randomPointNode();
        var connection  = new PointRelation(12D, 13D, targetPoint);
        var secondPoint = PointFixtures.randomPointNode();
        var actual      = target.saveAll(List.of(sourcePoint.add(connection), secondPoint));

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
        var graphName      = "test";

        target.saveAll(List.of(firstPoint, secondPoint, tooFarAwayPoint.add(randomRelation)));
        projectionService.createProjection(graphName);
        target.createConnections();

        var actual = testTemplate.allNodes();

        assertThat(projectionService.exists(graphName)).isFalse();
        assertThat(projectionService.defaultGraphName()).isNotEmpty();

        assertThat(actual)
                .hasSize(4)
                .anySatisfy(random -> assertThat(randomPoint)
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
                                .matches(relation -> relation.getWeight() == 5.247841282633755)
                                .matches(relation -> relation.getDistanceMeters() == 3.49856085508917)
                                .matches(relation -> relation.getTarget().equals(secondPoint))));
    }

}