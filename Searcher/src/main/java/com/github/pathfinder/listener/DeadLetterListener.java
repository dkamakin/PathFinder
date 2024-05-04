package com.github.pathfinder.listener;

import static com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration.Token.DEAD_LETTER_QUEUE_NAME;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.searcher.api.data.ConnectChunkMessage;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@AmqpListener(queues = DEAD_LETTER_QUEUE_NAME)
public class DeadLetterListener {

    private final ConnectionsQueueListener connectionsQueueListener;
    private final SaveChunksQueueListener  saveChunksQueueListener;

    @Logged
    @AmqpHandler
    public void createConnections(ConnectChunkMessage message) {
        connectionsQueueListener.connect(message);
    }

    @Logged
    @AmqpHandler
    public void save(SavePointsMessage request) {
        saveChunksQueueListener.save(request);
    }

}
