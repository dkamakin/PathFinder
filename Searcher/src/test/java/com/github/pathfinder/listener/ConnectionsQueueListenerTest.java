package com.github.pathfinder.listener;

import com.github.pathfinder.configuration.SearcherAmqpTest;
import com.github.pathfinder.messaging.MessagingTestConstant;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.ConnectChunkMessage;
import com.github.pathfinder.service.IPointConnector;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SearcherAmqpTest
class ConnectionsQueueListenerTest {

    @Autowired
    SearcherApi searcherApi;

    @MockBean
    IPointConnector pointConnector;

    @SpyBean
    DeadLetterListener deadLetterListener;

    void whenNeedToThrowOnCreateConnections(RuntimeException exception) {
        doThrow(exception).when(pointConnector).createConnections(any());
    }

    @Test
    @SneakyThrows
    void connect_ExceptionOccurred_Retry() {
        var chunk = 1;

        whenNeedToThrowOnCreateConnections(new RuntimeException());

        searcherApi.createConnections(new ConnectChunkMessage(chunk));

        verify(pointConnector, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis()).times(2))
                .createConnections(chunk);
        verify(deadLetterListener).createConnections(any());
    }

    @Test
    void connect_Request_CallService() {
        var chunk = 1;

        searcherApi.createConnections(new ConnectChunkMessage(chunk));

        verify(pointConnector, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis())).createConnections(chunk);
    }

}
