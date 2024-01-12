package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.ChunkPointRelation;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import com.github.pathfinder.service.IChunkUpdaterService;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@SearcherNeo4jTest
@Import(ChunkGetterService.class)
class ChunkGetterServiceTest {

    @Autowired
    IChunkUpdaterService chunkUpdaterService;

    @Autowired
    ChunkGetterService target;

    @Autowired
    Neo4jTestTemplate testTemplate;

    ChunkNode chunkNode(int id) {
        return ChunkNode.builder().id(id).build();
    }

    @Test
    void simple_NoChunksFound_EmptyList() {
        var actual = target.simple(List.of(1, 2, 3));

        assertThat(actual).isEmpty();
    }

    @Test
    void simple_SomeChunksFound_ReturnFoundChunks() {
        var point = PointFixtures.randomPointNode();
        var populatedChunk = ChunkNode.builder().id(9999)
                .pointRelations(Set.of(new ChunkPointRelation(point)))
                .build();
        var existingIds = List.of(123, 45, 898, populatedChunk.getId());

        testTemplate.save(point);
        existingIds.stream().map(this::chunkNode).forEach(chunkUpdaterService::save);

        var request = Stream.concat(existingIds.stream(), Stream.of(8345, 8123)).toList();
        var actual  = target.simple(request);

        assertThat(actual)
                .hasSameSizeAs(existingIds)
                .allMatch(chunk -> existingIds.contains(chunk.id()))
                .noneMatch(SimpleChunk::connected);
    }

}
