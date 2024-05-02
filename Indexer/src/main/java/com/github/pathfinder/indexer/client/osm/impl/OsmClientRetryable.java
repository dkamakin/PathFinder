package com.github.pathfinder.indexer.client.osm.impl;

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
        maxAttemptsExpression = "${osm.client.retry.maxAttempts}",
        backoff = @Backoff(
                delayExpression = "${osm.client.retry.delay}",
                maxDelayExpression = "${osm.client.retry.maxDelay}",
                multiplierExpression = "${osm.client.retry.multiplier}"
        )
)
public @interface OsmClientRetryable {
}
