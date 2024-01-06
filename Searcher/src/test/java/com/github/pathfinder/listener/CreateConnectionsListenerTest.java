package com.github.pathfinder.listener;

import com.github.pathfinder.configuration.SearcherAmqpTest;
import com.github.pathfinder.messaging.MessagingTestConstant;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.service.IPointService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SearcherAmqpTest
@Import({CreateConnectionsListener.class, DeadLetterListener.class})
class CreateConnectionsListenerTest {

    @MockBean
    IPointService pointService;

    @SpyBean
    DeadLetterListener deadLetterListener;

    @Autowired
    SearcherApi searcherApi;

    void whenNeedToThrow(RuntimeException exception) {
        doThrow(exception).when(pointService).createConnections();
    }

    @Test
    @SneakyThrows
    void connect_ExceptionOccurred_Retry() {
        whenNeedToThrow(new RuntimeException());

        searcherApi.createConnections();

        verify(pointService, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis()).times(2)).createConnections();
        verify(deadLetterListener).createConnections(any());
    }

    @Test
    void connect_Request_CallService() {
        searcherApi.createConnections();

        verify(pointService, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis())).createConnections();
    }

}
