package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.executor.PlatformExecutor;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.configuration.IndexerRetryConfiguration;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxSearcherService;
import com.github.pathfinder.indexer.service.osm.impl.OsmIndexTask;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.ConnectChunksMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class OsmIndexActor {

    private final SearcherApi               searcherApi;
    private final BoxSearcherService        boxSearcherService;
    private final IDateTimeSupplier         dateTimeSupplier;
    private final OsmIndexTask              task;
    private final PlatformExecutor          executor;
    private final IndexerRetryConfiguration retryConfiguration;

    @Logged
    @Transactional
    public void perform() {
        boxSearcherService.savable(retryConfiguration.getSaveDelay()).forEach(this::save);
        connect(boxSearcherService.connectable(retryConfiguration.getConnectDelay()));
    }

    private void save(IndexBoxEntity box) {
        executor.submit(() -> task.accept(box));
    }

    private void connect(List<IndexBoxEntity> boxes) {
        if (CollectionUtils.isEmpty(boxes)) {
            log.info("No boxes for connection");
            return;
        }

        searcherApi.createConnections(new ConnectChunksMessage(ids(boxes)));

        var timestamp = dateTimeSupplier.now();

        boxes.forEach(box -> box.setConnectionRequestTimestamp(timestamp));
    }

    private List<Integer> ids(List<IndexBoxEntity> boxes) {
        return boxes.stream().map(IndexBoxEntity::getId).toList();
    }

}
