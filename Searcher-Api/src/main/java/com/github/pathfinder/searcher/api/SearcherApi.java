package com.github.pathfinder.searcher.api;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.message.IMessageBuilder;
import com.github.pathfinder.messaging.router.IAMQPRouter;
import com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration;
import com.github.pathfinder.searcher.api.data.ConnectChunksMessage;
import com.github.pathfinder.searcher.api.data.GetChunksMessage;
import com.github.pathfinder.searcher.api.data.GetChunksResponse;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@RequiredArgsConstructor
public class SearcherApi {

    private final IAMQPRouter                    amqpRouter;
    private final SearcherMessagingConfiguration messagingConfiguration;
    private final IMessageBuilder                messageBuilder;

    @Logged
    public void save(SavePointsMessage request) {
        amqpRouter.route(messageBuilder.direct()
                                 .routingKey(messagingConfiguration.getDefaultQueueName())
                                 .with(request)).send();
    }

    @Logged
    public void createConnections(ConnectChunksMessage request) {
        amqpRouter.route(messageBuilder.direct()
                                 .routingKey(messagingConfiguration.getDefaultQueueName())
                                 .with(request)).send();
    }

    @Logged
    public GetChunksResponse chunks(GetChunksMessage request) {
        return amqpRouter.route(messageBuilder.direct()
                                        .routingKey(messagingConfiguration.getDefaultQueueName())
                                        .with(request)).sendAndReceive(GetChunksResponse.class);
    }

}
