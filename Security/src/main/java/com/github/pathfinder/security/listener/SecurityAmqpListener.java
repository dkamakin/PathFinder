package com.github.pathfinder.security.listener;

import com.github.pathfinder.messaging.error.RethrowingToSenderErrorHandler;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.security.api.configuration.SecurityQueueConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AmqpListener(errorHandler = RethrowingToSenderErrorHandler.NAME, queues = SecurityQueueConfiguration.Token.QUEUE_NAME)
public @interface SecurityAmqpListener {

}
