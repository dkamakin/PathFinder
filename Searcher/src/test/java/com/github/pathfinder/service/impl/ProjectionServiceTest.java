package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.exception.ProjectionAlreadyExistsException;
import com.github.pathfinder.service.IProjectionService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SearcherNeo4jTest
@Import({PointService.class, ProjectionService.class})
class ProjectionServiceTest {

    @Autowired
    PointService pointService;

    @Autowired
    IProjectionService target;

    @Test
    void createProjection_ProjectionDoesNotExist_CreateProjection() {
        var connection      = PointFixtures.pointConnection();
        var sourcePoint     = PointFixtures.pointBuilder().connections(Set.of(connection)).build();
        var graphName       = "test";
        var secondGraphName = graphName + 'a';

        pointService.save(sourcePoint);

        var actual = target.createProjection(graphName);

        assertThatThrownBy(() -> target.createProjection(graphName))
                .isInstanceOf(ProjectionAlreadyExistsException.class);

        target.createProjection(secondGraphName);

        assertThat(actual).matches(response -> response.nodesCount() == 2);
    }

    @Test
    void exists_ProjectionDoesNotExist_False() {
        assertThat(target.exists("random")).isFalse();
    }

    @Test
    void exists_ProjectionExists_True() {
        var connection  = PointFixtures.pointConnection();
        var sourcePoint = PointFixtures.pointBuilder().connections(Set.of(connection)).build();
        var graphName   = "test";

        pointService.save(sourcePoint);
        target.createProjection(graphName);

        assertThat(target.exists(graphName)).isTrue();
    }

    @Test
    void tryDelete_ProjectionExists_DeleteProjection() {
        var connection  = PointFixtures.pointConnection();
        var sourcePoint = PointFixtures.pointBuilder().connections(Set.of(connection)).build();
        var graphName   = "test";

        pointService.save(sourcePoint);
        target.createProjection(graphName);

        assertThat(target.exists(graphName)).isTrue();

        target.tryDelete(graphName);

        assertThat(target.exists(graphName)).isFalse();
    }

    @Test
    void tryDelete_ProjectionDoesNotExist_NoAction() {
        assertThatCode(() -> target.tryDelete("test")).doesNotThrowAnyException();
    }

}
