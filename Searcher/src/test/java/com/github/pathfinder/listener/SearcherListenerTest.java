package com.github.pathfinder.listener;

import com.github.pathfinder.configuration.SearcherAmqpTest;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.exception.BadRequestException;
import com.github.pathfinder.core.exception.ErrorCode;
import com.github.pathfinder.core.exception.ServiceException;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.messaging.MessagingTestConstant;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.point.Point;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import com.github.pathfinder.service.IPointService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SearcherAmqpTest
class SearcherListenerTest {

    @Autowired
    SearcherApi searcherApi;

    @MockBean
    IPointService pointService;

    @SpyBean
    DeadLetterListener deadLetterListener;

    @Captor
    ArgumentCaptor<List<PointNode>> pointNodesCaptor;

    void whenNeedToSave(List<PointNode> expected) {
        when(pointService.saveAll(any())).thenReturn(expected);
    }

    void whenNeedToThrowOnSaveAll(RuntimeException exception) {
        when(pointService.saveAll(any())).thenThrow(exception);
    }

    void whenNeedToThrowOnCreateConnections(RuntimeException exception) {
        doThrow(exception).when(pointService).createConnections();
    }

    @Test
    @SneakyThrows
    void connect_ExceptionOccurred_Retry() {
        whenNeedToThrowOnCreateConnections(new RuntimeException());

        searcherApi.createConnections();

        verify(pointService, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis()).times(2)).createConnections();
        verify(deadLetterListener).createConnections(any());
    }

    @Test
    void connect_Request_CallService() {
        searcherApi.createConnections();

        verify(pointService, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis())).createConnections();
    }

    @Test
    void save_ExceptionOccurred_RethrowException() {
        whenNeedToThrowOnSaveAll(new BadRequestException("test"));

        var message = new SavePointsMessage(List.of());

        assertThatThrownBy(() -> searcherApi.save(message))
                .isInstanceOf(ServiceException.class)
                .satisfies(exception ->
                                   assertThat(((ServiceException) exception).errorCode())
                                           .isEqualTo(ErrorCode.BAD_REQUEST));
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

        whenNeedToSave(pointNodes);

        int actual = searcherApi.save(new SavePointsMessage(List.of(point)));

        verify(pointService).saveAll(pointNodesCaptor.capture());

        assertThat(pointNodesCaptor.getValue())
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(UUID.class)
                .isEqualTo(expected);

        assertThat(actual).isEqualTo(pointNodes.size());
    }

}
