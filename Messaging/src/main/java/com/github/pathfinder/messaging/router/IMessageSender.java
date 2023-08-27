package com.github.pathfinder.messaging.router;

import java.util.Optional;

public interface IMessageSender {

    <T> void send(String queue, T data);

    <R, T> Optional<T> sendAndReceive(String queue, R data, Class<T> expected);

}
