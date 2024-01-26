package com.github.pathfinder.listener;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.searcher.api.data.ConnectChunkMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration.Token.DEAD_LETTER_QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
@AmqpListener(queues = DEAD_LETTER_QUEUE_NAME)
public class DeadLetterListener {

    private final ConnectionsQueueListener connectionsQueueListener;

    @Logged
    @AmqpHandler
    public void createConnections(ConnectChunkMessage message) {
        connectionsQueueListener.connect(message);
    }

}
