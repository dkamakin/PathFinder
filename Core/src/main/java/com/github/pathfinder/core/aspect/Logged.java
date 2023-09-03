package com.github.pathfinder.core.aspect;

import com.github.pathfinder.core.log.LogLevel;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logged {

    LogLevel logLevel() default LogLevel.INFO;

    String[] value() default {};

    boolean ignoreReturnValue() default true;

    boolean logException() default false;

    boolean logExecutionTime() default true;

}
