package com.github.pathfinder.service.impl;

import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.PointRelation;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestFile {

    private final TestPathFile                          deserialized;
    private final Map<UUID, TestPathFile.TestFilePoint> pointsMap;
    private final List<PointNode>                       nodes;

    public TestFile(TestPathFile deserialized) {
        this.deserialized = deserialized;
        this.pointsMap    = pointsMap(deserialized);
        this.nodes        = deserialized.points().stream().map(this::pointNode).toList();
    }

    public PointNode node(UUID id) {
        return nodes.stream()
                .filter(node -> node.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Point not found: " + id));
    }

    public List<PointNode> nodes() {
        return deserialized.points().stream().map(this::pointNode).toList();
    }

    private PointNode pointNode(TestPathFile.TestFilePoint point) {
        return PointNode.builder()
                .id(point.id())
                .relations(relations(point.connections()))
                .landType(point.landType())
                .passabilityCoefficient(1D)
                .location(point.latitude(), point.longitude(), point.altitude())
                .build();
    }

    private Set<PointRelation> relations(Set<TestPathFile.TestFilePoint.Connection> connections) {
        return connections.stream().map(this::relation).collect(Collectors.toSet());
    }

    private PointRelation relation(TestPathFile.TestFilePoint.Connection connection) {
        var point  = point(connection.targetId());
        var entity = pointNode(point);

        return new PointRelation(connection.meters(), connection.weight(), entity);
    }

    private TestPathFile.TestFilePoint point(UUID id) {
        return Optional.ofNullable(pointsMap.get(id))
                .orElseThrow(() -> new IllegalArgumentException("Point not found: " + id));
    }

    private Map<UUID, TestPathFile.TestFilePoint> pointsMap(TestPathFile testPathFile) {
        return testPathFile.points().stream()
                .collect(Collectors.toMap(TestPathFile.TestFilePoint::id, Function.identity()));
    }

}
