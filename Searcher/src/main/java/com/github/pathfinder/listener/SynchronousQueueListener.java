package com.github.pathfinder.listener;

import static com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration.Token.SYNCHRONOUS_QUEUE_NAME;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.mapper.NodeMapper;
import com.github.pathfinder.messaging.error.RethrowingToSenderErrorHandler;
import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.searcher.api.data.GetChunksMessage;
import com.github.pathfinder.searcher.api.data.GetChunksResponse;
import com.github.pathfinder.service.impl.ChunkGetterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@AmqpListener(queues = SYNCHRONOUS_QUEUE_NAME, errorHandler = RethrowingToSenderErrorHandler.NAME)
public class SynchronousQueueListener {

    private final ChunkGetterService chunkGetterService;

    @AmqpHandler
    @Logged("request")
    public GetChunksResponse chunks(GetChunksMessage request) {
        return NodeMapper.MAPPER.getChunksResponse(chunkGetterService.simple(request.ids()));
    }

}
