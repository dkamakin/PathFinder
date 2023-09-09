package com.github.pathfinder.security.api;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.message.IMessage;
import com.github.pathfinder.messaging.message.IMessageBuilder;
import com.github.pathfinder.messaging.router.IAMQPRouter;
import com.github.pathfinder.security.api.configuration.SecurityQueueConfiguration;
import com.github.pathfinder.security.api.data.SecurityApiMapper;
import com.github.pathfinder.security.api.data.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityApi {

    private final IAMQPRouter                amqpRouter;
    private final SecurityQueueConfiguration queueConfiguration;
    private final IMessageBuilder            messageBuilder;

    @Logged
    public UserInfo userInfo(String token) {
        return amqpRouter
                .route(directMessage(SecurityApiMapper.INSTANCE.userInfoRequest(token)))
                .sendAndReceive(UserInfo.class);
    }

    private <T> IMessage<T> directMessage(T data) {
        return messageBuilder
                .direct()
                .routingKey(queueConfiguration.getQueueName())
                .with(data);
    }
}
