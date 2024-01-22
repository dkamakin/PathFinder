package com.github.pathfinder.database.node;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.types.GeographicPoint2d;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Node(ChunkNode.Token.NODE_NAME)
public class ChunkNode {

    @UtilityClass
    public static class Token {

        public static final String NODE_NAME           = "Chunk";
        public static final String ID                  = "id";
        public static final String CONNECTED           = "connected";
        public static final String MIN                 = "min";
        public static final String MAX                 = "max";
        public static final String POINT_RELATION_TYPE = "IN_CHUNK";

    }

    @Id
    @GeneratedValue
    private String internalId;

    @Property(Token.ID)
    private int id;

    @Property(Token.CONNECTED)
    private boolean connected;

    @NotNull
    @Property(Token.MIN)
    private GeographicPoint2d min;

    @NotNull
    @Property(Token.MAX)
    private GeographicPoint2d max;

    @NotEmpty
    @Relationship(type = Token.POINT_RELATION_TYPE)
    private List<PointNode> points;

    public ChunkNode(int id, GeographicPoint2d min, GeographicPoint2d max, List<PointNode> points) {
        this.id     = id;
        this.min    = min;
        this.max    = max;
        this.points = points;
    }

    public static ChunkNodeBuilder builder() {
        return new ChunkNodeBuilder();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("internalId", internalId)
                .add("id", id)
                .add("connected", connected)
                .add("min", min)
                .add("max", max)
                .add("pointRelations", points.size())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChunkNode chunkNode = (ChunkNode) o;
        return id == chunkNode.id &&
                connected == chunkNode.connected &&
                Objects.equal(min, chunkNode.min) &&
                Objects.equal(max, chunkNode.max);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, connected, min, max);
    }

}
