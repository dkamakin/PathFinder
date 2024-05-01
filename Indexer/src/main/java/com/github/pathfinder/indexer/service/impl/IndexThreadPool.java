package com.github.pathfinder.indexer.service.impl;

import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class IndexThreadPool {

    private final ExecutorService executor;

    public void accept(Runnable task) {
        executor.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                log.error("Failed to execute", e);
            }
        });
    }

}
