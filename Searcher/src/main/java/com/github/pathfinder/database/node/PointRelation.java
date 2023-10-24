package com.github.pathfinder.database.node;

import com.google.common.base.Objects;
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

        public static final String DISTANCE = "distance";
    }

    @RelationshipId
    private String id;

    @Property(Token.DISTANCE)
    private Double distance;

    @TargetNode
    private PointNode target;

    public PointRelation(Double distance, PointNode target) {
        this.distance = distance;
        this.target   = target;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        PointRelation that = (PointRelation) object;
        return Objects.equal(distance, that.distance) &&
                Objects.equal(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(distance, target);
    }
}
