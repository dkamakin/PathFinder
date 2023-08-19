package com.github.pathfinder.core.aspect;

import com.github.pathfinder.core.interfaces.IThrowingSupplier;
import com.github.pathfinder.core.tools.MethodTimer;
import java.time.Duration;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggedAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggedAspect.class);

    @Around("@annotation(Logged)")
    public Object logged(ProceedingJoinPoint joinPoint) throws Throwable {
        var annotation = annotation(joinPoint);
        var logger     = logger(annotation);

        logger.accept("Executing: {}", joinPoint.getSignature().toShortString());

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
                LOG.error("Exception caught", e);
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
            case INFO -> LOG::info;
            case WARN -> LOG::warn;
            case DEBUG -> LOG::debug;
            case ERROR -> LOG::error;
        };
    }

    private Logged annotation(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Logged.class);
    }

    private interface LogConsumer {

        void accept(String message, Object... params);

    }
}
