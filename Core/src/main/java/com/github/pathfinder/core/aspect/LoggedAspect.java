package com.github.pathfinder.core.aspect;

import java.time.Duration;
import com.github.pathfinder.core.interfaces.IThrowingSupplier;
import com.github.pathfinder.core.tools.impl.MethodTimer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggedAspect {

    private final LoggedFormatter formatter;

    @Around("@annotation(Logged)")
    public Object logged(ProceedingJoinPoint joinPoint) throws Throwable {
        var annotation = annotation(joinPoint);

        if (annotation == null) {
            return joinPoint.proceed();
        }

        var logger     = logger(annotation);
        var methodName = formatter.methodName(joinPoint.getSignature());
        var header     = formatter.header(methodName, annotation.value(), joinPoint.getArgs());

        logger.accept("Executing {}", header);

        var result = proceed(joinPoint::proceed, annotation, logger, methodName);

        if (isNeedToLogResult(annotation)) {
            logger.accept("{} returned {}", methodName, result);
        }

        return result;
    }

    private Object proceed(IThrowingSupplier<Object, Throwable> method,
                           Logged annotation,
                           LogConsumer logger,
                           String methodName) throws Throwable {
        try {
            if (annotation.logExecutionTime()) {
                return new MethodTimer(duration -> logExecutionTime(duration, logger, methodName)).throwable(method);
            } else {
                return method.get();
            }
        } catch (Throwable e) {
            if (annotation.logException()) {
                log.error("Method threw an exception", e);
            }

            throw e;
        }
    }

    private void logExecutionTime(Duration duration, LogConsumer logger, String methodName) {
        logger.accept("{} execution time {}", methodName, duration);
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

    private interface LogConsumer {

        void accept(String message, Object... params);

    }
}
