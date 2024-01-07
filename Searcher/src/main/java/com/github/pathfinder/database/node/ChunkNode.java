package com.github.pathfinder.database.node;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Getter
@ToString
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

    public ChunkNode(int id) {
        this.id = id;
    }

    public void connected() {
        this.connected = true;
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
