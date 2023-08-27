package com.github.pathfinder.messaging.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.annotation.AliasFor;

@Documented
@RabbitListener
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {

    @AliasFor(annotation = RabbitListener.class, attribute = "queues")
    String[] queues() default {};

}
