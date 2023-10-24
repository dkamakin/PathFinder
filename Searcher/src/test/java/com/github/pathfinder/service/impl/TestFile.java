package com.github.pathfinder.service.impl;

import com.github.pathfinder.database.entity.PointEntity;
import com.github.pathfinder.database.entity.PointRelation;
import com.github.pathfinder.exception.PointNotFoundException;
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
    private final List<PointEntity>                     entities;

    public TestFile(TestPathFile deserialized) {
        this.deserialized = deserialized;
        this.pointsMap    = pointsMap(deserialized);
        this.entities     = deserialized.points().stream().map(this::pointEntity).toList();
    }

    public PointEntity entity(UUID id) {
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PointNotFoundException(id));
    }

    public List<PointEntity> pointEntities() {
        return deserialized.points().stream().map(this::pointEntity).toList();
    }

    private PointEntity pointEntity(TestPathFile.TestFilePoint point) {
        return new PointEntity(point.id(), relations(point), point.altitude(), point.longitude(), point.latitude(),
                               point.landType());
    }

    private Set<PointRelation> relations(TestPathFile.TestFilePoint point) {
        return point.connections().stream().map(this::relation).collect(Collectors.toSet());
    }

    private PointRelation relation(TestPathFile.TestFilePoint.TestFileConnection connection) {
        var point  = point(connection.targetId());
        var entity = pointEntity(point);

        return new PointRelation(connection.distance(), entity);
    }

    private TestPathFile.TestFilePoint point(UUID id) {
        return Optional.ofNullable(pointsMap.get(id)).orElseThrow(() -> new PointNotFoundException(id));
    }

    private Map<UUID, TestPathFile.TestFilePoint> pointsMap(TestPathFile testPathFile) {
        return testPathFile.points().stream()
                .collect(Collectors.toMap(TestPathFile.TestFilePoint::id, Function.identity()));
    }

}
