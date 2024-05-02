package com.github.pathfinder.indexer.client.elevation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.pathfinder.indexer.exception.ApiExecutionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Retryable(
        retryFor = ApiExecutionException.class,
        maxAttemptsExpression = "${elevation.client.retry.maxAttempts}",
        backoff = @Backoff(
                delayExpression = "${elevation.client.retry.delay}",
                maxDelayExpression = "${elevation.client.retry.maxDelay}",
                multiplierExpression = "${elevation.client.retry.multiplier}"
        )
)
public @interface ElevationClientRetryable {
}
