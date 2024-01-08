package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.service.IProjectionService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

@SearcherNeo4jTest
class ProjectionServiceTest {

    @Autowired
    IProjectionService target;

    @Autowired
    Neo4jTestTemplate testTemplate;

    @Test
    void deleteAll_ProjectionsExists_DeleteAll() {
        var sourcePoint     = PointFixtures.randomPointNode();
        var connection      = new PointRelation(12D, 13D, PointFixtures.randomPointNode());
        var graphName       = "test";
        var secondGraphName = graphName + 'a';

        testTemplate.saveAll(List.of(sourcePoint.add(connection)));

        target.createProjection(graphName);
        target.createProjection(secondGraphName);

        assertThat(target.deleteAll()).hasSize(2).contains(graphName, secondGraphName);
    }

    @Test
    void deleteAll_NoProjections_EmptyResult() {
        assertThat(target.deleteAll()).isEmpty();
    }

    @Test
    void createProjection_ProjectionDoesNotExist_CreateProjection() {
        var sourcePoint     = PointFixtures.randomPointNode();
        var connection      = new PointRelation(12D, 13D, PointFixtures.randomPointNode());
        var graphName       = "test";
        var secondGraphName = graphName + 'a';

        testTemplate.saveAll(List.of(sourcePoint.add(connection)));

        assertThat(target.createProjection(graphName)).isTrue();
        assertThat(target.createProjection(graphName)).isFalse();
        assertThat(target.createProjection(secondGraphName)).isTrue();
    }

    @Test
    void exists_ProjectionDoesNotExist_False() {
        assertThat(target.exists("random")).isFalse();
    }

    @Test
    void exists_ProjectionExists_True() {
        var sourcePoint = PointFixtures.randomPointNode();
        var connection  = new PointRelation(12D, 13D, PointFixtures.randomPointNode());
        var graphName   = "test";

        testTemplate.saveAll(List.of(sourcePoint.add(connection)));
        target.createProjection(graphName);

        assertThat(target.exists(graphName)).isTrue();
    }

}
