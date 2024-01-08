package com.github.pathfinder.database.node;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

public class ChunkNodeBuilder {

    private Integer                 id;
    private Set<ChunkPointRelation> pointRelations;

    public ChunkNodeBuilder() {
        this.pointRelations = Collections.emptySet();
    }

    public ChunkNodeBuilder id(Integer id) {
        return setAndReturnThis(id, x -> this.id = x);
    }

    public ChunkNodeBuilder pointRelations(Set<ChunkPointRelation> relations) {
        return setAndReturnThis(relations, x -> this.pointRelations = x);
    }

    public ChunkNode build() {
        return new ChunkNode(id, pointRelations);
    }

    private <T> ChunkNodeBuilder setAndReturnThis(T value, Consumer<T> consumer) {
        consumer.accept(value);
        return this;
    }

}
