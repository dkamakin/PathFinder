package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.Indexer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndexTask {

    private final Indexer         indexer;
    private final IndexThreadPool threadPool;

    public void accept(IndexBoxEntity box) {
        threadPool.accept(() -> indexer.process(box));
    }

}
