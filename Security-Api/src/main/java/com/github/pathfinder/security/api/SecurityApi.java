package com.github.pathfinder.security.api;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.message.IMessageBuilder;
import com.github.pathfinder.messaging.router.IAMQPRouter;
import com.github.pathfinder.security.api.configuration.SecurityMessagingConfiguration;
import com.github.pathfinder.security.api.data.SecurityApiMapper;
import com.github.pathfinder.security.api.data.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@RequiredArgsConstructor
public class SecurityApi {

    private final IAMQPRouter                    amqpRouter;
    private final SecurityMessagingConfiguration messagingConfiguration;
    private final IMessageBuilder                messageBuilder;

    @Logged
    public UserInfo userInfo(String token) {
        return amqpRouter
                .route(messageBuilder
                               .direct()
                               .routingKey(messagingConfiguration.getDefaultQueueName())
                               .with(SecurityApiMapper.INSTANCE.userInfoRequest(token)))
                .sendAndReceive(UserInfo.class);
    }

}
