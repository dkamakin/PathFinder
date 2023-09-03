package com.github.pathfinder.messaging.router;

import com.github.pathfinder.messaging.message.IMessage;

public interface IAMQPRouter {

    <T> IRouter route(IMessage<T> message);

    interface IRouter {

        <T> T sendAndReceive(Class<T> expected);

    }

}
