package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.configuration.IndexerRetryConfiguration;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.ConnectChunkMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndexActor {

    private final SearcherApi               searcherApi;
    private final IndexBoxSearcherService   boxSearcherService;
    private final IDateTimeSupplier         dateTimeSupplier;
    private final IndexTask                 task;
    private final IndexerRetryConfiguration retryConfiguration;

    @Logged
    @Transactional
    public void perform() {
        boxSearcherService.savable(retryConfiguration.getSaveDelay()).forEach(task::accept);
        boxSearcherService.connectable(retryConfiguration.getConnectDelay()).forEach(this::connect);
    }

    private void connect(IndexBoxEntity box) {
        try {
            searcherApi.createConnections(new ConnectChunkMessage(box.getId()));

            box.setConnectionRequestTimestamp(dateTimeSupplier.now());
        } catch (Exception e) {
            log.error("Failed to send {} for connection: {}", box, e.getMessage());
        }
    }

}
