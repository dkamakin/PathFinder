package com.github.pathfinder.messaging.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.annotation.AliasFor;

@RabbitListener
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AmqpListener {

    @AliasFor(annotation = RabbitListener.class, attribute = "containerFactory")
    String containerFactory() default "";

    @AliasFor(annotation = RabbitListener.class, attribute = "queues")
    String[] queues() default {};

    @AliasFor(annotation = RabbitListener.class, attribute = "errorHandler")
    String errorHandler() default "";

}
