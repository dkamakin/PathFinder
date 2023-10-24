package com.github.pathfinder.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.database.entity.PointEntity;
import com.github.pathfinder.database.repository.impl.PathRepository;
import com.github.pathfinder.exception.PathNotFoundException;
import com.github.pathfinder.service.IPathSearcher;
import com.github.pathfinder.service.IPointService;
import com.github.pathfinder.service.IProjectionService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SearcherNeo4jTest
@Import({PointService.class, ProjectionService.class, PathSearcher.class, PathRepository.class})
class PathSearcherTest {

    static Path RESOURCES      = Paths.get("src", "test", "resources");
    static Path TEST_FILE_PATH = RESOURCES.resolve("paths.json");

    @Autowired
    IPointService pointService;

    @Autowired
    IProjectionService projectionService;

    @Autowired
    IPathSearcher target;

    @Autowired
    Neo4jTestTemplate neo4jTestTemplate;

    @BeforeEach
    void setUp() {
        neo4jTestTemplate.cleanDatabase();
    }

    @SneakyThrows
    static Stream<TestPathFile> testPathFileStream() {
        var json   = Files.readString(TEST_FILE_PATH);
        var mapper = CoreConfiguration.objectMapperFactory();

        return mapper.readValue(json, new TypeReference<List<TestPathFile>>() {
        }).stream();
    }

    @ParameterizedTest
    @MethodSource("testPathFileStream")
    void aStar_PathExists_ReturnCorrectPath(TestPathFile deserialized) {
        var graphName = "test path exists";
        var testFile  = new TestFile(deserialized);

        testFile.pointEntities().forEach(pointService::save);

        var sourcePoint = testFile.entity(deserialized.sourceId());
        var targetPoint = testFile.entity(deserialized.targetId());

        projectionService.createProjection(graphName);

        var actual = target.aStar(graphName, sourcePoint, targetPoint);

        assertThat(actual)
                .satisfies(found -> assertThat(found.path())
                        .map(PointEntity::getId)
                        .isEqualTo(deserialized.expected().path()))
                .matches(found -> found.totalCost().equals(deserialized.expected().totalCost()));
    }

    @Test
    void aStar_PathDoesNotExist_PathNotFoundException() {
        var graphName   = "test path does not exist";
        var sourcePoint = pointService.save(PointFixtures.pointWithConnection());
        var targetPoint = pointService.save(PointFixtures.point());

        projectionService.createProjection(graphName);

        assertThatThrownBy(() -> target.aStar(graphName, sourcePoint, targetPoint))
                .isInstanceOf(PathNotFoundException.class);
    }

}
