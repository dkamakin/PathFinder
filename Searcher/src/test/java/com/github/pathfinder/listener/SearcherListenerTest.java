package com.github.pathfinder.listener;

import com.github.pathfinder.configuration.SearcherAmqpTest;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.exception.BadRequestException;
import com.github.pathfinder.core.exception.ErrorCode;
import com.github.pathfinder.core.exception.ServiceException;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.messaging.MessagingTestConstant;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.Chunk;
import com.github.pathfinder.searcher.api.data.ConnectChunksMessage;
import com.github.pathfinder.searcher.api.data.GetChunksMessage;
import com.github.pathfinder.searcher.api.data.point.Point;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import com.github.pathfinder.service.IPointConnector;
import com.github.pathfinder.service.IPointService;
import com.github.pathfinder.service.impl.ChunkService;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SearcherAmqpTest
class SearcherListenerTest {

    @Autowired
    SearcherApi searcherApi;

    @MockBean
    IPointConnector pointConnector;

    @MockBean
    IPointService pointService;

    @SpyBean
    DeadLetterListener deadLetterListener;

    @MockBean
    ChunkService chunkService;

    @Captor
    ArgumentCaptor<List<PointNode>> pointNodesCaptor;

    void whenNeedToSave(List<PointNode> expected) {
        when(pointService.saveAll(anyInt(), any())).thenReturn(expected);
    }

    void whenNeedToThrowOnCreateConnections(RuntimeException exception) {
        doThrow(exception).when(pointConnector).createConnections(anyList());
    }

    void whenNeedToGetChunks(List<Integer> ids, List<ChunkNode> expected) {
        when(chunkService.chunks(ids)).thenReturn(expected);
    }

    void whenNeedToThrowOnGetSavedChunks(RuntimeException expected) {
        when(chunkService.chunks(anyList())).thenThrow(expected);
    }

    @Test
    void find_Request_CallService() {
        var message  = new GetChunksMessage(List.of(1, 2, 3));
        var node     = ChunkNode.builder().id(1).build();
        var expected = new Chunk(node.getId(), node.isConnected());

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
    @SneakyThrows
    void connect_ExceptionOccurred_Retry() {
        var chunks = List.of(1, 2, 3);

        whenNeedToThrowOnCreateConnections(new RuntimeException());

        searcherApi.createConnections(new ConnectChunksMessage(chunks));

        verify(pointConnector, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis()).times(2))
                .createConnections(chunks);
        verify(deadLetterListener).createConnections(any());
    }

    @Test
    void connect_Request_CallService() {
        var chunks = List.of(1, 2, 3);

        searcherApi.createConnections(new ConnectChunksMessage(chunks));

        verify(pointConnector, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis())).createConnections(chunks);
    }

    @Test
    void save_ValidRequest_CallService() {
        var point = new Point(1D, new Coordinate(2D, 3D), "BAY", 4D);
        var expected = PointNode.builder()
                .landType(point.landType())
                .location(point.coordinate().latitude(), point.coordinate().longitude(), point.altitude())
                .passabilityCoefficient(point.passabilityCoefficient())
                .build();
        var pointNodes = List.of(expected);
        var id         = 1;

        whenNeedToSave(pointNodes);

        searcherApi.save(new SavePointsMessage(id, List.of(point)));

        verify(pointService, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis()))
                .saveAll(eq(id), pointNodesCaptor.capture());

        assertThat(pointNodesCaptor.getValue())
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(UUID.class)
                .isEqualTo(expected);
    }

}
