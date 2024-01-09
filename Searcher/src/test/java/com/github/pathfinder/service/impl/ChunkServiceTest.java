package com.github.pathfinder.service.impl;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.ChunkPointRelation;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

@SearcherNeo4jTest
class ChunkServiceTest {

    @Autowired
    ChunkService target;

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
    void chunks_NoChunksFound_EmptyList() {
        var actual = target.chunks(List.of(1, 2, 3));

        assertThat(actual).isEmpty();
    }

    @Test
    void chunks_SomeChunksFound_ReturnFoundChunks() {
        var point = PointFixtures.randomPointNode();
        var populatedChunk = ChunkNode.builder().id(9999)
                .pointRelations(Set.of(new ChunkPointRelation(point)))
                .build();
        var existingIds = List.of(123, 45, 898, populatedChunk.getId());

        testTemplate.save(point);
        existingIds.stream().map(this::chunkNode).forEach(target::save);

        var request = Stream.concat(existingIds.stream(), Stream.of(8345, 8123)).toList();
        var actual  = target.chunks(request);

        assertThat(actual)
                .hasSameSizeAs(existingIds)
                .allMatch(chunk -> chunk.getPointRelations().isEmpty())
                .allMatch(chunk -> existingIds.contains(chunk.getId()))
                .allMatch(chunk -> StringUtils.isNotEmpty(chunk.getInternalId()))
                .noneMatch(ChunkNode::isConnected);
    }

    @Test
    void extendedChunks_ChunkHasRelations_ReturnWithRelations() {
        var point    = PointFixtures.randomPointNode();
        var relation = new ChunkPointRelation(point);
        var populatedChunk = ChunkNode.builder().id(9999)
                .pointRelations(Set.of(relation))
                .build();

        testTemplate.save(point);
        target.save(populatedChunk);

        var actual = target.extendedChunks(List.of(populatedChunk.getId()));

        assertThat(actual)
                .hasSize(1)
                .first()
                .satisfies(chunk -> assertThat(chunk.getPointRelations())
                        .hasSize(1)
                        .contains(relation));
    }

    @Test
    void markConnected_SomeChunksAreMarked_UpdateChunks() {
        var markedChunkIds    = List.of(123, 45, 898);
        var notMarkedChunkIds = List.of(999, 98);

        Stream.concat(markedChunkIds.stream(), notMarkedChunkIds.stream()).map(this::chunkNode).forEach(target::save);

        target.markConnected(markedChunkIds, true);

        assertThat(target.chunks(markedChunkIds)).allMatch(ChunkNode::isConnected);
        assertThat(target.chunks(notMarkedChunkIds)).noneMatch(ChunkNode::isConnected);
    }

}
