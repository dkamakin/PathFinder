package com.github.pathfinder.listener;

import static com.github.pathfinder.messaging.test.MessagingTestConstant.DEFAULT_TIMEOUT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import com.github.pathfinder.configuration.SearcherAmqpTest;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.ConnectChunkMessage;
import com.github.pathfinder.service.impl.PointConnector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SearcherAmqpTest
class ConnectionsQueueListenerTest {

    @Autowired
    SearcherApi searcherApi;

    @MockBean
    PointConnector pointConnector;

    @SpyBean
    DeadLetterListener deadLetterListener;

    void whenNeedToThrowOnCreateConnections(RuntimeException exception) {
        doThrow(exception).when(pointConnector).createConnections(any());
    }

    @Test
    void connect_ExceptionOccurred_Retry() {
        var request = new ConnectChunkMessage(1);

        whenNeedToThrowOnCreateConnections(new RuntimeException());

        searcherApi.createConnections(request);

        verify(pointConnector, timeout(DEFAULT_TIMEOUT.toMillis()).times(2)).createConnections(request.id());
        verify(deadLetterListener).createConnections(request);
    }

    @Test
    void connect_Request_CallService() {
        var chunk = 1;

        searcherApi.createConnections(new ConnectChunkMessage(chunk));

        verify(pointConnector, timeout(DEFAULT_TIMEOUT.toMillis())).createConnections(chunk);
    }

}
