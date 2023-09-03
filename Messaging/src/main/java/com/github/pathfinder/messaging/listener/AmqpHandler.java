package com.github.pathfinder.messaging.listener;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;

@Documented
@RabbitHandler
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AmqpHandler {

}
