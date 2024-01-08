package com.github.pathfinder.database.node;

import com.google.common.base.Objects;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class ChunkPointRelation {

    @UtilityClass
    public static class Token {

        public static final String TYPE = "IN_CHUNK";

    }

    @RelationshipId
    private String id;

    @NotNull
    @TargetNode
    private PointNode target;

    public ChunkPointRelation(PointNode target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChunkPointRelation that = (ChunkPointRelation) o;
        return Objects.equal(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(target);
    }

}
