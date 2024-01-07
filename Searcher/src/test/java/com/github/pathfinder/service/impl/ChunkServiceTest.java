package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.database.node.ChunkNode;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

@SearcherNeo4jTest
class ChunkServiceTest {

    @Autowired
    ChunkService target;

    ChunkNode chunkNode(int id) {
        return new ChunkNode(id);
    }

    @Test
    void save_ChunkNode_SaveAndReturnPopulatedInstance() {
        var node = chunkNode(1234);

        var actual = target.save(node);

        assertThat(actual)
                .matches(Predicate.not(ChunkNode::isConnected))
                .matches(x -> StringUtils.isNotEmpty(x.getInternalId()))
                .matches(x -> x.getId() == node.getId());
    }

    @Test
    void chunks_NoChunksFound_EmptyList() {
        var actual = target.chunks(List.of(1, 2, 3));

        assertThat(actual).isEmpty();
    }

    @Test
    void chunks_SomeChunksFound_ReturnFoundChunks() {
        var existingIds = List.of(123, 45, 898);

        existingIds.stream().map(this::chunkNode).forEach(target::save);

        var request = Stream.concat(existingIds.stream(), Stream.of(8345, 8123)).toList();
        var actual  = target.chunks(request);

        assertThat(actual)
                .hasSameSizeAs(existingIds)
                .allMatch(node -> existingIds.contains(node.getId()))
                .allMatch(node -> StringUtils.isNotEmpty(node.getInternalId()))
                .allMatch(Predicate.not(ChunkNode::isConnected));
    }

}
