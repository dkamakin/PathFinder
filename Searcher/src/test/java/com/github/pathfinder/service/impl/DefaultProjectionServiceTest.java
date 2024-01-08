package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.service.IDefaultProjectionService;
import com.github.pathfinder.service.IProjectionService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

@SearcherNeo4jTest
class DefaultProjectionServiceTest {

    @Autowired
    IProjectionService projectionService;

    @Autowired
    IDefaultProjectionService target;

    @Autowired
    Neo4jTestTemplate testTemplate;

    @Test
    void createDefaultProjection_MultipleCalls_CreateSingleProjection() {
        var sourcePoint = PointFixtures.randomPointNode();
        var connection  = new PointRelation(12D, 13D, PointFixtures.randomPointNode());

        testTemplate.saveAll(List.of(sourcePoint.add(connection)));

        var graphName = target.createDefaultProjection();

        assertThat(graphName).isEqualTo(target.createDefaultProjection());

        assertThat(projectionService.exists(graphName)).isTrue();
        assertThat(projectionService.deleteAll()).hasSize(1).first().isEqualTo(graphName);
    }

    @Test
    void defaultGraphName_NoProjections_EmptyResult() {
        assertThat(target.defaultGraphName()).isEmpty();
    }

    @Test
    void defaultGraphName_ProjectionsExists_ReturnDefault() {
        var sourcePoint = PointFixtures.randomPointNode();
        var connection  = new PointRelation(12D, 13D, PointFixtures.randomPointNode());

        testTemplate.saveAll(List.of(sourcePoint.add(connection)));

        var defaultGraphName = target.createDefaultProjection();
        var anotherGraphName = defaultGraphName + 'a';

        assertThat(projectionService.createProjection(anotherGraphName)).isTrue();
        assertThat(target.defaultGraphName()).contains(defaultGraphName);
    }

}
