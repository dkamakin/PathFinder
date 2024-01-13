package com.github.pathfinder.core.executor.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.executor.PlatformExecutor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UnboundPlatformExecutor implements PlatformExecutor {

    private static final String THREAD_PREFIX = "platform-";

    private final ExecutorService executor;

    public UnboundPlatformExecutor() {
        this.executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name(THREAD_PREFIX, 0L).factory());
    }

    @Logged
    @Override
    public void submit(Runnable task) {
        executor.submit(task);
    }
}
