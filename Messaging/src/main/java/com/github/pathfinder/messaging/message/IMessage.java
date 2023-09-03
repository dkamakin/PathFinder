package com.github.pathfinder.messaging.message;

public interface IMessage<T> {

    String exchangeName();

    String routingKey();

    T data();

}
