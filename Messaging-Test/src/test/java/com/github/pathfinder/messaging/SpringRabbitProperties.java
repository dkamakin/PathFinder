package com.github.pathfinder.messaging;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SpringRabbitProperties {

    private static final class Token {
        static final String USERNAME = "spring.rabbitmq.username";
        static final String PASSWORD = "spring.rabbitmq.password";
        static final String HOST     = "spring.rabbitmq.host";
        static final String PORT     = "spring.rabbitmq.port";
    }

    public void setUsername(String username) {
        set(Token.USERNAME, username);
    }

    public void setPassword(String password) {
        set(Token.PASSWORD, password);
    }

    public void setHost(String host) {
        set(Token.HOST, host);
    }

    public void setPort(String port) {
        set(Token.PORT, port);
    }

    public void set(String key, String value) {
        System.setProperty(key, value);
    }

}
