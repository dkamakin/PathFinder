package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilder;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxService;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.Chunk;
import com.github.pathfinder.searcher.api.data.GetChunksMessage;
import com.github.pathfinder.searcher.api.data.GetChunksResponse;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@IndexerServiceDatabaseTest
@Import({ActualizeService.class, IndexBoxService.class})
class ActualizeServiceTest {

    @Autowired
    ActualizeService target;

    @MockBean
    SearcherApi searcherApi;

    @Autowired
    IndexerStateBuilder stateBuilder;

    @Autowired
    BoxService boxService;

    void whenNeedToGetChunks(GetChunksMessage message, GetChunksResponse response) {
        when(searcherApi.chunks(message)).thenReturn(response);
    }

    @Test
    void perform_BoxesNotFound_NoAction() {
        target.perform();

        verifyNoInteractions(searcherApi);
    }

    @Test
    void perform_BoxWasNotFoundSinceLastTry_UpdateBoxState() {
        var id = stateBuilder.randomCoords(IndexBoxEntity.builder().connected(true).saved(true)).getId();

        whenNeedToGetChunks(new GetChunksMessage(List.of(id)), new GetChunksResponse(List.of()));

        assertThat(boxService.all())
                .hasSize(1)
                .first()
                .matches(box -> box.getId().equals(id))
                .matches(IndexBoxEntity::isConnected)
                .matches(IndexBoxEntity::isSaved);

        target.perform();

        assertThat(boxService.all())
                .hasSize(1)
                .first()
                .matches(box -> box.getId().equals(id))
                .matches(Predicate.not(IndexBoxEntity::isConnected))
                .matches(Predicate.not(IndexBoxEntity::isSaved));
    }

    @Test
    void perform_ChunkWasSavedSinceTheLastTry_UpdateChunkState() {
        var id = stateBuilder.randomCoords(IndexBoxEntity.builder().connected(false).saved(false)).getId();

        whenNeedToGetChunks(new GetChunksMessage(List.of(id)),
                            new GetChunksResponse(List.of(new Chunk(id, false))));

        assertThat(boxService.all())
                .hasSize(1)
                .first()
                .matches(box -> box.getId().equals(id))
                .matches(Predicate.not(IndexBoxEntity::isConnected))
                .matches(Predicate.not(IndexBoxEntity::isSaved));

        target.perform();

        assertThat(boxService.all())
                .hasSize(1)
                .first()
                .matches(box -> box.getId().equals(id))
                .matches(Predicate.not(IndexBoxEntity::isConnected))
                .matches(IndexBoxEntity::isSaved);
    }

    @Test
    void perform_ChunkWasSavedAndConnectedSinceLastTry_UpdateChunkState() {
        var id = stateBuilder.randomCoords(IndexBoxEntity.builder().connected(false).saved(false)).getId();

        whenNeedToGetChunks(new GetChunksMessage(List.of(id)), new GetChunksResponse(List.of(new Chunk(id, true))));

        assertThat(boxService.all())
                .hasSize(1)
                .first()
                .matches(box -> box.getId().equals(id))
                .matches(Predicate.not(IndexBoxEntity::isConnected))
                .matches(Predicate.not(IndexBoxEntity::isSaved));

        target.perform();

        assertThat(boxService.all())
                .hasSize(1)
                .first()
                .matches(box -> box.getId().equals(id))
                .matches(IndexBoxEntity::isConnected)
                .matches(IndexBoxEntity::isSaved);
    }

    @Test
    void perform_ChunkWasSavedAndConnectedRecently_CheckStateAgain() {
        var id = stateBuilder.randomCoords(IndexBoxEntity.builder()
                                                   .connected(false)
                                                   .saved(false)
                                                   .connectionRequestTimestamp(Instant.now())
                                                   .saveRequestTimestamp(Instant.now())).getId();

        whenNeedToGetChunks(new GetChunksMessage(List.of(id)), new GetChunksResponse(List.of(new Chunk(id, false))));

        assertThat(boxService.all())
                .hasSize(1)
                .first()
                .matches(box -> box.getId().equals(id))
                .matches(Predicate.not(IndexBoxEntity::isConnected))
                .matches(Predicate.not(IndexBoxEntity::isSaved));

        target.perform();

        assertThat(boxService.all())
                .hasSize(1)
                .first()
                .matches(box -> box.getId().equals(id))
                .matches(Predicate.not(IndexBoxEntity::isConnected))
                .matches(IndexBoxEntity::isSaved);
    }

}
