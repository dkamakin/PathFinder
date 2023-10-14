package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.data.point.Point;
import com.github.pathfinder.exception.ProjectionAlreadyExistsException;
import com.github.pathfinder.exception.ProjectionNotFoundException;
import com.github.pathfinder.service.IProjectionService;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SearcherNeo4jTest
@Import({PointService.class, ProjectionService.class})
class ProjectionServiceTest {

    @Autowired
    PointService pointService;

    @Autowired
    IProjectionService target;

    @Autowired
    Neo4jTestTemplate neo4jTestTemplate;

    @BeforeEach
    void setUp() {
        neo4jTestTemplate.cleanDatabase();
    }

    @Test
    void createProjection_ProjectionDoesNotExist_CreateProjection() {
        var targetPoint     = PointFixtures.pointBuilder().altitude(1D).build();
        var connection      = new Point.PointConnection(targetPoint, 1D);
        var sourcePoint     = PointFixtures.pointBuilder().connections(Set.of(connection)).build();
        var graphName       = "test";
        var secondGraphName = graphName + 'a';

        pointService.save(sourcePoint);

        var actual = target.createProjection(graphName);

        assertThatThrownBy(() -> target.createProjection(graphName))
                .isInstanceOf(ProjectionAlreadyExistsException.class);

        target.createProjection(secondGraphName);

        target.delete(secondGraphName);
        target.delete(graphName);

        assertThat(actual)
                .matches(response -> response.nodesCount() == 2);
    }

    @Test
    void exists_ProjectionDoesNotExist_False() {
        assertThat(target.exists("random")).isFalse();
    }

    @Test
    void exists_ProjectionExists_True() {
        var targetPoint = PointFixtures.pointBuilder().altitude(1D).build();
        var connection  = new Point.PointConnection(targetPoint, 1D);
        var sourcePoint = PointFixtures.pointBuilder().connections(Set.of(connection)).build();
        var graphName   = "test does exist";

        pointService.save(sourcePoint);
        target.createProjection(graphName);

        assertThat(target.exists(graphName)).isTrue();

        target.delete(graphName);
    }

    @Test
    void delete_ProjectionExists_DeleteProjection() {
        var targetPoint = PointFixtures.pointBuilder().altitude(1D).build();
        var connection  = new Point.PointConnection(targetPoint, 1D);
        var sourcePoint = PointFixtures.pointBuilder().connections(Set.of(connection)).build();
        var graphName   = "test";

        pointService.save(sourcePoint);
        target.createProjection(graphName);

        assertThat(target.exists(graphName)).isTrue();

        target.delete(graphName);

        assertThat(target.exists(graphName)).isFalse();
    }

    @Test
    void delete_ProjectionDoesNotExist_ProjectionNotFoundException() {
        assertThatThrownBy(() -> target.delete("sdasd"))
                .isInstanceOf(ProjectionNotFoundException.class);
    }

}
