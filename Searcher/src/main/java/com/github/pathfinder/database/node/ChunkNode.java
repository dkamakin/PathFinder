package com.github.pathfinder.database.node;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Node(ChunkNode.Token.NODE_NAME)
public class ChunkNode {

    @UtilityClass
    public static class Token {

        public static final String NODE_NAME = "Chunk";
        public static final String ID        = "id";
        public static final String CONNECTED = "connected";

    }

    @Id
    @GeneratedValue
    private String internalId;

    @Property(Token.ID)
    private int id;

    @Property(Token.CONNECTED)
    private boolean connected;

    @Relationship(type = ChunkPointRelation.Token.TYPE)
    private Set<ChunkPointRelation> pointRelations;

    protected ChunkNode(int id, Set<ChunkPointRelation> pointRelations) {
        this.pointRelations = pointRelations;
        this.id             = id;
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
                .add("pointRelations", pointRelations.size())
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
        return id == chunkNode.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
