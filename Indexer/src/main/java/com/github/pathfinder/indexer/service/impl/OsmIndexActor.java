package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.executor.PlatformExecutor;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.configuration.IndexerConfiguration;
import com.github.pathfinder.indexer.data.EntityMapper;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxService;
import com.github.pathfinder.indexer.service.osm.impl.OsmIndexTask;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.ConnectChunksMessage;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class OsmIndexActor {

    private final SearcherApi          searcherApi;
    private final BoxService           boxService;
    private final IDateTimeSupplier    dateTimeSupplier;
    private final OsmIndexTask         task;
    private final PlatformExecutor     executor;
    private final IndexerConfiguration indexerConfiguration;

    @Logged
    @Transactional
    public void perform() {
        var notSavedOrConnected = notSavedOrConnected();

        if (CollectionUtils.isEmpty(notSavedOrConnected)) {
            log.info("Not saved or connected boxes not found");
            return;
        }

        notSavedOrConnected.stream()
                .filter(Predicate.not(IndexBoxEntity::isSaved))
                .forEach(this::save);

        if (isNeedToConnect(notSavedOrConnected)) {
            connect(notSavedOrConnected);
        }
    }

    private boolean isNeedToConnect(List<IndexBoxEntity> boxes) {
        var allConnected = true;

        for (var box : boxes) {
            if (!box.isSaved()) {
                log.info("No need to connect: some of the boxes are not saved");
                return false;
            }

            if (!box.isConnected()) {
                log.info("Detected a not connected box: {}", box);
                allConnected = false;
            }
        }

        return !allConnected;
    }

    private List<IndexBoxEntity> notSavedOrConnected() {
        return boxService.notSavedOrConnected(indexerConfiguration.getRetryChunkSaveDelay(),
                                              indexerConfiguration.getRetryChunkConnectDelay());
    }

    private void save(IndexBoxEntity box) {
        executor.submit(() -> task.accept(EntityMapper.MAPPER.indexBox(box)));
    }

    private void connect(List<IndexBoxEntity> boxes) {
        var ids = ids(boxes);

        searcherApi.createConnections(new ConnectChunksMessage(ids));

        var timestamp = dateTimeSupplier.instant();

        boxService.boxes(ids).forEach(box -> box.setConnectionRequestTimestamp(timestamp));
    }

    private List<Integer> ids(List<IndexBoxEntity> boxes) {
        return boxes.stream().map(IndexBoxEntity::getId).toList();
    }

}
