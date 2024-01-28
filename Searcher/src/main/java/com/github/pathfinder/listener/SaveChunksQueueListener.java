package com.github.pathfinder.listener;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.mapper.NodeMapper;
import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import com.github.pathfinder.service.IChunkUpdaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration.Token.SAVE_CHUNKS_LISTENER_QUEUE_FACTORY_NAME;
import static com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration.Token.SAVE_CHUNKS_QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
@AmqpListener(queues = SAVE_CHUNKS_QUEUE_NAME, containerFactory = SAVE_CHUNKS_LISTENER_QUEUE_FACTORY_NAME)
public class SaveChunksQueueListener {

    private final IChunkUpdaterService chunkUpdaterService;

    @AmqpHandler
    @Logged("request")
    public void save(SavePointsMessage request) {
        chunkUpdaterService.save(NodeMapper.MAPPER.chunkNode(request.box(), request.points()));
    }

}
