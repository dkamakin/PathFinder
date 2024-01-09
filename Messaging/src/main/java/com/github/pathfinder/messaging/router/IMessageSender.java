package com.github.pathfinder.messaging.router;

import com.github.pathfinder.messaging.message.IMessage;
import java.util.Optional;

public interface IMessageSender {

    <R, T> Optional<T> sendAndReceive(IMessage<R> message, Class<T> expected);

    <T> void send(IMessage<T> message);

}
