package com.github.pathfinder.core.executor.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.exception.InternalServerException;
import com.github.pathfinder.core.executor.PlatformExecutor;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UnboundPlatformExecutor implements PlatformExecutor {

    private final ExecutorService executor;

    public UnboundPlatformExecutor() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Logged
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        try {
            return executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Failed to invoke all tasks", e);
            throw new InternalServerException("Failed to invoke tasks: %s", e.getMessage());
        }
    }
}
