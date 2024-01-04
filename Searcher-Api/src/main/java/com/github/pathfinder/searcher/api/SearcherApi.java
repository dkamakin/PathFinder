package com.github.pathfinder.searcher.api;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.message.IMessageBuilder;
import com.github.pathfinder.messaging.router.IAMQPRouter;
import com.github.pathfinder.searcher.api.configuration.SearcherMessagingConfiguration;
import com.github.pathfinder.searcher.api.data.point.CreateConnectionsMessage;
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
    public Integer save(SavePointsMessage request) {
        return amqpRouter.route(messageBuilder.direct()
                                        .routingKey(messagingConfiguration.getDefaultQueueName())
                                        .with(request)).sendAndReceive(Integer.class);
    }

    @Logged
    public void createConnections() {
        amqpRouter.route(messageBuilder.direct()
                                 .routingKey(messagingConfiguration.getDefaultQueueName())
                                 .with(new CreateConnectionsMessage())).send();
    }

}
