package com.github.pathfinder.messaging.router.impl;

import com.github.pathfinder.messaging.message.IMessage;
import com.github.pathfinder.messaging.router.IAMQPRouter;
import com.github.pathfinder.messaging.router.IMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AMQPRouter implements IAMQPRouter {

    private final IMessageSender messageSender;

    @Override
    public <T> IRouter route(IMessage<T> message) {
        return new Router(messageSender, message);
    }

}
