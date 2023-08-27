package com.github.pathfinder.messaging.router.impl;

import com.github.pathfinder.messaging.router.IMessageRouter;
import com.github.pathfinder.messaging.router.IMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * TODO: logic with message queue could be improved by using a separated router microservice
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageRouter implements IMessageRouter {

    private final IMessageSender messageSender;

    @Override
    public IDataSetter message(String queue) {
        return new DataSetter(queue, messageSender);
    }

}
