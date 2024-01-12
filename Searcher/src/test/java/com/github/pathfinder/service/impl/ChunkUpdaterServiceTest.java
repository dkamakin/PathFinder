package com.github.pathfinder.service.impl;

import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@SearcherNeo4jTest
@Import(ChunkGetterService.class)
class ChunkUpdaterServiceTest {

    @Autowired
    ChunkUpdaterService target;

    @Autowired
    ChunkGetterService chunkGetterService;

    @Autowired
    Neo4jTestTemplate testTemplate;

    ChunkNode chunkNode(int id) {
        return ChunkNode.builder().id(id).build();
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
    void markConnected_SomeChunksAreMarked_UpdateChunks() {
        var markedChunkIds    = List.of(123, 45, 898);
        var notMarkedChunkIds = List.of(999, 98);

        Stream.concat(markedChunkIds.stream(), notMarkedChunkIds.stream()).map(this::chunkNode).forEach(target::save);

        target.markConnected(markedChunkIds, true);

        assertThat(chunkGetterService.simple(markedChunkIds)).allMatch(SimpleChunk::connected);
        assertThat(chunkGetterService.simple(notMarkedChunkIds)).noneMatch(SimpleChunk::connected);
    }

}
