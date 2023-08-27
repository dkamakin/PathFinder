package com.github.pathfinder.messaging.router.impl;

import com.github.pathfinder.messaging.router.IMessageRouter;
import com.github.pathfinder.messaging.router.IMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DataSetter implements IMessageRouter.IDataSetter {

    private final String         queue;
    private final IMessageSender messageSender;

    @Override
    public <T> IMessageRouter.ISender with(T data) {
        return new Sender(messageSender, queue, data);
    }
}
