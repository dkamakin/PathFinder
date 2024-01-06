package com.github.pathfinder.listener;

import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.searcher.api.data.point.CreateConnectionsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration.Token.DEAD_LETTER_QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
@AmqpListener(queues = DEAD_LETTER_QUEUE_NAME)
public class DeadLetterListener {

    private final SearcherListener searcherListener;

    @AmqpHandler
    public void createConnections(CreateConnectionsMessage message) {
        searcherListener.connect(message);
    }

}
