package com.github.pathfinder.messaging.router.impl;

import com.github.pathfinder.messaging.exception.MessagingTimeoutException;
import com.github.pathfinder.messaging.message.IMessage;
import com.github.pathfinder.messaging.router.IAMQPRouter;
import com.github.pathfinder.messaging.router.IMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Router implements IAMQPRouter.IRouter {

    private final IMessageSender messageSender;
    private final IMessage<?>    message;

    @Override
    public <T> T sendAndReceive(Class<T> expected) {
        return messageSender
                .sendAndReceive(message, expected)
                .orElseThrow(MessagingTimeoutException::new);
    }

    @Override
    public void send() {
        messageSender.send(message);
    }

}
