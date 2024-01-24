package com.github.pathfinder.listener;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.searcher.api.data.ConnectChunkMessage;
import com.github.pathfinder.service.IPointConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration.Token.CONNECTIONS_LISTENER_QUEUE_FACTORY_NAME;
import static com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration.Token.CONNECTIONS_QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
@AmqpListener(queues = CONNECTIONS_QUEUE_NAME, containerFactory = CONNECTIONS_LISTENER_QUEUE_FACTORY_NAME)
public class ConnectionsQueueListener {

    private final IPointConnector pointConnector;

    @AmqpHandler
    @Logged("request")
    public void connect(ConnectChunkMessage request) {
        pointConnector.createConnections(request.id());
    }

}
