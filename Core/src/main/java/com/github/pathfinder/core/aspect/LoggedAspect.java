package com.github.pathfinder.core.aspect;

import com.github.pathfinder.core.interfaces.IThrowingSupplier;
import com.github.pathfinder.core.tools.MethodTimer;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggedAspect {

    @Around("@annotation(Logged)")
    public Object logged(ProceedingJoinPoint joinPoint) throws Throwable {
        var annotation = annotation(joinPoint);
        var logger     = logger(annotation);

        logger.accept("Executing: {}", joinPoint.getSignature().toShortString());

        if (ArrayUtils.isNotEmpty(annotation.arguments())) {
            logger.accept(formatArguments(annotation.arguments(), joinPoint.getArgs()));
        }

        var result = proceed(joinPoint::proceed, annotation, logger);

        if (isNeedToLogResult(annotation)) {
            logger.accept("Returned: {}", result);
        }

        return result;
    }

    private Object proceed(IThrowingSupplier<Object, Throwable> method,
                           Logged annotation,
                           LogConsumer logger) throws Throwable {
        try {
            if (annotation.logExecutionTime()) {
                return new MethodTimer(duration -> logExecutionTime(duration, logger)).throwable(method);
            } else {
                return method.get();
            }
        } catch (Throwable e) {
            if (annotation.logException()) {
                log.error("Exception caught", e);
            }

            throw e;
        }
    }

    private void logExecutionTime(Duration duration, LogConsumer logger) {
        logger.accept("Execution time: {}", duration);
    }

    private boolean isNeedToLogResult(Logged annotation) {
        return !annotation.ignoreReturnValue();
    }

    private LogConsumer logger(Logged annotation) {
        return switch (annotation.logLevel()) {
            case INFO -> log::info;
            case WARN -> log::warn;
            case DEBUG -> log::debug;
            case ERROR -> log::error;
        };
    }

    private Logged annotation(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Logged.class);
    }

    private String formatArguments(String[] names, Object[] arguments) {
        var result = new StringBuilder();

        for (var i = 0; i < names.length; i++) {
            var name = names[i];

            if (StringUtils.isEmpty(name)) {
                continue;
            }

            result.append(name).append("=").append(arguments[i]);

            if (i != names.length - 1) {
                result.append(", ");
            }
        }

        return result.toString();
    }

    private interface LogConsumer {

        void accept(String message, Object... params);

    }
}
