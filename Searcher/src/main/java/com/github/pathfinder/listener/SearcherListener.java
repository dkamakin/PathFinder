package com.github.pathfinder.listener;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.mapper.NodeMapper;
import com.github.pathfinder.messaging.error.RethrowingToSenderErrorHandler;
import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.searcher.api.data.ConnectChunksMessage;
import com.github.pathfinder.searcher.api.data.GetChunksMessage;
import com.github.pathfinder.searcher.api.data.GetChunksResponse;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import com.github.pathfinder.service.IChunkGetterService;
import com.github.pathfinder.service.IPointConnector;
import com.github.pathfinder.service.IPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration.Token.DEFAULT_QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
@AmqpListener(queues = DEFAULT_QUEUE_NAME, errorHandler = RethrowingToSenderErrorHandler.NAME)
public class SearcherListener {

    private final IPointService       pointService;
    private final IChunkGetterService chunkGetterService;
    private final IPointConnector     pointConnector;

    @AmqpHandler
    @Logged("request")
    public void connect(ConnectChunksMessage request) {
        pointConnector.createConnections(request.ids());
    }

    @AmqpHandler
    @Logged("request")
    public void save(SavePointsMessage request) {
        pointService.saveAll(request.id(), NodeMapper.MAPPER.pointNodes(request.points()));
    }

    @AmqpHandler
    @Logged("request")
    public GetChunksResponse chunks(GetChunksMessage request) {
        return NodeMapper.MAPPER.getChunksResponse(chunkGetterService.simple(request.ids()));
    }

}
