package com.github.pathfinder.messaging.message.impl;

import com.github.pathfinder.messaging.message.IMessage;

public record Message<T>(String exchangeName,
                         String routingKey,
                         T data) implements IMessage<T> {

}
