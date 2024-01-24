package com.github.pathfinder.listener;

import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.SearcherAmqpTest;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.exception.BadRequestException;
import com.github.pathfinder.core.exception.ErrorCode;
import com.github.pathfinder.core.exception.ServiceException;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import com.github.pathfinder.messaging.MessagingTestConstant;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.Chunk;
import com.github.pathfinder.searcher.api.data.GetChunksMessage;
import com.github.pathfinder.searcher.api.data.IndexBox;
import com.github.pathfinder.searcher.api.data.point.Point;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import com.github.pathfinder.service.IChunkGetterService;
import com.github.pathfinder.service.IChunkUpdaterService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SearcherAmqpTest
class DefaultQueueListenerTest {

    @Autowired
    SearcherApi searcherApi;

    @Autowired
    IChunkUpdaterService chunkUpdaterService;

    @Autowired
    IChunkGetterService chunkGetterService;

    @Captor
    ArgumentCaptor<ChunkNode> chunkNodeCaptor;

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

    @Test
    void save_ValidRequest_CallService() {
        var point = new Point(1D, new Coordinate(2D, 3D), "BAY", 4D);
        var pointNode = PointNode.builder()
                .landType(point.landType())
                .location(point.coordinate().latitude(), point.coordinate().longitude(), point.altitude())
                .passabilityCoefficient(point.passabilityCoefficient())
                .build();
        var box = new IndexBox(1, PointFixtures.randomCoordinate(), PointFixtures.randomCoordinate());
        var chunk = ChunkNode.builder()
                .id(box.id())
                .min(box.min())
                .max(box.max())
                .points(List.of(pointNode))
                .build();

        searcherApi.save(new SavePointsMessage(box, List.of(point)));

        verify(chunkUpdaterService, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis()))
                .save(chunkNodeCaptor.capture());

        assertThat(chunkNodeCaptor.getValue())
                .isEqualTo(chunk)
                .satisfies(x -> assertThat(x.getPoints())
                        .hasSize(1)
                        .first()
                        .usingRecursiveComparison()
                        .ignoringFieldsOfTypes(UUID.class)
                        .isEqualTo(pointNode));
    }

}
