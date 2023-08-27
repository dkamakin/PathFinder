package com.github.pathfinder.messaging.router.impl;

import com.github.pathfinder.messaging.exception.ReceiveMessageException;
import com.github.pathfinder.messaging.router.IMessageRouter;
import com.github.pathfinder.messaging.router.IMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Sender implements IMessageRouter.ISender {

    private final IMessageSender messageSender;
    private final String         queue;
    private final Object         data;

    @Override
    public void send() {
        messageSender.send(queue, data);
    }

    @Override
    public <T> T sendAndReceive(Class<T> expected) {
        return messageSender
                .sendAndReceive(queue, data, expected)
                .orElseThrow(() -> new ReceiveMessageException(queue));
    }

}
