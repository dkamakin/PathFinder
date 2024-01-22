package com.github.pathfinder.database.node;

import com.github.pathfinder.core.tools.impl.NullHelper;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import org.springframework.data.neo4j.types.GeographicPoint3d;

public class PointNodeBuilder {

    private UUID               id;
    private Set<PointRelation> relations;
    private GeographicPoint3d  location;
    private String             landType;
    private Double             passabilityCoefficient;

    public PointNodeBuilder() {
        this.relations = Sets.newHashSet();
    }

    public PointNodeBuilder id(UUID id) {
        return setAndReturnThis(id, x -> this.id = x);
    }

    public PointNodeBuilder relations(Set<PointRelation> relations) {
        return setAndReturnThis(relations, x -> this.relations = x);
    }

    public PointNodeBuilder passabilityCoefficient(Double passabilityCoefficient) {
        return setAndReturnThis(passabilityCoefficient, x -> this.passabilityCoefficient = x);
    }

    public PointNodeBuilder landType(String landType) {
        return setAndReturnThis(landType, x -> this.landType = x);
    }

    public PointNodeBuilder location(Double latitude, Double longitude, Double altitude) {
        return setAndReturnThis(location, x -> this.location = new GeographicPoint3d(latitude, longitude, altitude));
    }

    public PointNode build() {
        return new PointNode(NullHelper.notNull(id, UUID::randomUUID), relations, location, landType,
                             passabilityCoefficient);
    }

    private <T> PointNodeBuilder setAndReturnThis(T value, Consumer<T> consumer) {
        consumer.accept(value);
        return this;
    }

}
