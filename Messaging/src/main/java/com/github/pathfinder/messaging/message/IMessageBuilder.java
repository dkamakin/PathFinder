package com.github.pathfinder.messaging.message;

public interface IMessageBuilder {

    IRoutingKeySetter direct();

    interface IRoutingKeySetter {

        IMessageSetter routingKey(String routingKey);

    }

    interface IMessageSetter {

        <T> IMessage<T> with(T data);

    }

}
