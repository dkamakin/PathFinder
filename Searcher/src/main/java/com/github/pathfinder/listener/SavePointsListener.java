package com.github.pathfinder.listener;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.mapper.NodeMapper;
import com.github.pathfinder.messaging.error.RethrowingToSenderErrorHandler;
import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import com.github.pathfinder.service.IPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration.Token.DEFAULT_QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
@AmqpListener(queues = DEFAULT_QUEUE_NAME, errorHandler = RethrowingToSenderErrorHandler.NAME)
public class SavePointsListener {

    private final IPointService pointService;

    @Logged
    @AmqpHandler
    public int save(SavePointsMessage request) {
        return pointService.saveAll(NodeMapper.MAPPER.pointNodes(request.points())).size();
    }

}
