package com.github.pathfinder.service.impl;

import java.util.List;
import java.util.function.Predicate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.Neo4jTestTemplate;
import com.github.pathfinder.configuration.SearcherNeo4jTest;
import com.github.pathfinder.core.exception.BadRequestException;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointRelation;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import io.micrometer.common.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

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
    void save_ChunkNodeAlreadySaved_BadRequestException() {
        var node = chunkNode(1234);

        target.save(node);

        assertThatThrownBy(() -> target.save(node)).isInstanceOf(BadRequestException.class);
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
    void save_ChunkWithPoint_SaveChunkAndPoint() {
        var sourcePoint   = PointFixtures.randomPointNode();
        var targetPoint   = PointFixtures.randomPointNode();
        var pointRelation = new PointRelation(1.2, 1.3, targetPoint);

        var node = ChunkNode.builder()
                .id(1)
                .points(List.of(sourcePoint.add(pointRelation)))
                .build();

        assertThat(testTemplate.allPointNodes()).isEmpty();

        var actual = target.save(node);

        assertThat(actual)
                .matches(Predicate.not(ChunkNode::isConnected))
                .matches(x -> StringUtils.isNotEmpty(x.getInternalId()))
                .matches(x -> x.getId() == node.getId());

        assertThat(testTemplate.allPointNodes())
                .hasSize(2)
                .anySatisfy(source -> assertThat(source)
                        .matches(x -> x.getId().equals(source.getId()))
                        .satisfies(x -> assertThat(x.getRelations())
                                .hasSize(1)
                                .first()
                                .matches(relation -> relation.getTarget().equals(targetPoint))));
    }

    @Test
    void markConnected_ChunkExists_UpdateChunk() {
        var chunk = target.save(chunkNode(1233));

        target.markConnected(chunk.getId(), true);

        assertThat(chunkGetterService.simple(List.of(chunk.getId())))
                .first()
                .matches(SimpleChunk::connected);
    }

}
