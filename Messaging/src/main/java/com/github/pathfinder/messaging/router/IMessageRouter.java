package com.github.pathfinder.messaging.router;

public interface IMessageRouter {

    IDataSetter message(String queue);

    interface IDataSetter {

        <T> ISender with(T data);

    }

    interface ISender {

        void send();

        <T> T sendAndReceive(Class<T> expected);

    }

}
