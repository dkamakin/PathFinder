package com.github.pathfinder.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.database.repository.impl.PathRepository;
import com.github.pathfinder.searcher.api.exception.PathNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.core.Neo4jClient;

@SearcherNeo4jTest
@Import({PathSearcher.class, PathRepository.class})
class PathSearcherTest {

    static Path RESOURCES      = Paths.get("src", "test", "resources");
    static Path TEST_FILE_PATH = RESOURCES.resolve("paths.json");

    @Autowired
    PathSearcher target;

    @Autowired
    Neo4jTestTemplate testTemplate;

    @Autowired
    Neo4jClient client;

    @SneakyThrows
    static Stream<TestPathFile> testPathFileStream() {
        var json   = Files.readString(TEST_FILE_PATH);
        var mapper = CoreConfiguration.objectMapperFactory();

        return mapper.readValue(json, new TypeReference<List<TestPathFile>>() {
        }).stream();
    }

    Coordinate coordinate(PointNode node) {
        return new Coordinate(node.latitude(), node.longitude());
    }

    @ParameterizedTest
    @MethodSource("testPathFileStream")
    void aStar_PathExists_ReturnCorrectPath(TestPathFile deserialized) {
        var testFile = new TestFile(deserialized);

        testTemplate.saveAll(testFile.nodes());

        var sourcePoint = testFile.node(deserialized.sourceId());
        var targetPoint = testFile.node(deserialized.targetId());
        var request     = new FindPathRequest(coordinate(sourcePoint), coordinate(targetPoint));

        var actual   = target.aStar(request);
        var expected = deserialized.expected();

        assertThat(actual)
                .satisfies(found -> assertThat(found.path())
                        .map(PointNode::getId)
                        .hasSameSizeAs(expected.path())
                        .containsSequence(expected.path()))
                .satisfies(found -> assertThat(found.meters())
                        .isEqualTo(expected.meters()))
                .satisfies(found -> assertThat(found.weight())
                        .isEqualTo(expected.weight()));
    }

    @Test
    void aStar_PathDoesNotExist_PathNotFoundException() {
        var sourcePoint = PointFixtures.randomPointNode();
        var targetPoint = PointFixtures.randomPointNode();
        var request     = new FindPathRequest(coordinate(sourcePoint), coordinate(targetPoint));

        testTemplate.saveAll(List.of(sourcePoint, targetPoint));

        assertThatThrownBy(() -> target.aStar(request)).isInstanceOf(PathNotFoundException.class);
    }

    @Test
    void aStar_PointsAreConnectedOppositeWay_PathFoundAnyway() {
        var sourcePoint = PointFixtures.randomPointNode();
        var targetPoint = PointFixtures.randomPointNode();
        var relation    = new PointRelation(1.5, 0.5, sourcePoint);

        testTemplate.saveAll(List.of(sourcePoint, targetPoint.add(relation)));

        assertThat(target.aStar(new FindPathRequest(coordinate(sourcePoint), coordinate(targetPoint))))
                .satisfies(actual -> assertThat(actual.path())
                        .hasSize(2)
                        .contains(sourcePoint, targetPoint))
                .satisfies(actual -> assertThat(actual.meters()).isEqualTo(relation.getDistanceMeters()))
                .satisfies(actual -> assertThat(actual.weight()).isEqualTo(relation.getWeight()));

        assertThat(target.aStar(new FindPathRequest(coordinate(targetPoint), coordinate(sourcePoint))))
                .satisfies(actual -> assertThat(actual.path())
                        .hasSize(2)
                        .contains(sourcePoint, targetPoint))
                .satisfies(actual -> assertThat(actual.meters()).isEqualTo(relation.getDistanceMeters()))
                .satisfies(actual -> assertThat(actual.weight()).isEqualTo(relation.getWeight()));
    }

}
