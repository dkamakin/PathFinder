package com.github.pathfinder.security.listener;

import com.github.pathfinder.messaging.error.ErrorHandlers;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.security.api.configuration.SecurityQueueConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@AmqpListener(errorHandler = ErrorHandlers.RETHROWING_HANDLER, queues = SecurityQueueConfiguration.Token.QUEUE_NAME)
public @interface SecurityAmqpListener {

}
