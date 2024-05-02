package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.Indexer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndexTask {

    private final Indexer                indexer;
    private final IndexThreadPool        threadPool;
    private final IndexBoxUpdaterService boxUpdaterService;
    private final IDateTimeSupplier      dateTimeSupplier;

    @Transactional
    public void accept(IndexBoxEntity box) {
        boxUpdaterService.save(box.setSaveRequestTimestamp(dateTimeSupplier.now()));
        threadPool.accept(() -> indexer.process(box.getId()));
    }

}
