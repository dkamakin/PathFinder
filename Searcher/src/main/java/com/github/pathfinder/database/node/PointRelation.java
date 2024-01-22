package com.github.pathfinder.database.node;

import com.google.common.base.Objects;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class PointRelation {

    @UtilityClass
    public static class Token {

        public static final String DISTANCE_METERS = "distanceMeters";
        public static final String WEIGHT          = "weight";
        public static final String TYPE            = "CONNECTION";

    }

    @RelationshipId
    private String id;

    @Property(Token.DISTANCE_METERS)
    private double distanceMeters;

    @Property(Token.WEIGHT)
    private double weight;

    @NotNull
    @TargetNode
    private PointNode target;

    public PointRelation(Double distanceMeters, Double weight, PointNode target) {
        this.distanceMeters = distanceMeters;
        this.weight         = weight;
        this.target         = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PointRelation that = (PointRelation) o;
        return Objects.equal(distanceMeters, that.distanceMeters) &&
                Objects.equal(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(distanceMeters, weight);
    }

}
