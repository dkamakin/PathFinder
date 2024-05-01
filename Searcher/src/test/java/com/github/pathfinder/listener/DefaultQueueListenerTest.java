package com.github.pathfinder.listener;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import com.github.pathfinder.configuration.SearcherAmqpTest;
import com.github.pathfinder.core.exception.BadRequestException;
import com.github.pathfinder.core.exception.ErrorCode;
import com.github.pathfinder.core.exception.ServiceException;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.Chunk;
import com.github.pathfinder.searcher.api.data.GetChunksMessage;
import com.github.pathfinder.service.impl.ChunkGetterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SearcherAmqpTest
class DefaultQueueListenerTest {

    @Autowired
    SearcherApi searcherApi;

    @Autowired
    ChunkGetterService chunkGetterService;

    void whenNeedToGetChunks(List<Integer> ids, List<SimpleChunk> expected) {
        when(chunkGetterService.simple(ids)).thenReturn(expected);
    }

    void whenNeedToThrowOnGetSavedChunks(RuntimeException expected) {
        when(chunkGetterService.simple(anyList())).thenThrow(expected);
    }

    @Test
    void chunks_Request_CallService() {
        var message  = new GetChunksMessage(List.of(1, 2, 3));
        var node     = new SimpleChunk(1, true);
        var expected = new Chunk(node.id(), node.connected());

        whenNeedToGetChunks(message.ids(), List.of(node));

        var actual = searcherApi.chunks(message);

        assertThat(actual.chunks()).hasSize(1).first().isEqualTo(expected);
    }

    @Test
    void existing_ExceptionOccurred_RethrowException() {
        whenNeedToThrowOnGetSavedChunks(new BadRequestException("test"));

        var message = new GetChunksMessage(List.of());

        assertThatThrownBy(() -> searcherApi.chunks(message))
                .isInstanceOf(ServiceException.class)
                .satisfies(exception ->
                                   assertThat(((ServiceException) exception).errorCode())
                                           .isEqualTo(ErrorCode.BAD_REQUEST));
    }

}
