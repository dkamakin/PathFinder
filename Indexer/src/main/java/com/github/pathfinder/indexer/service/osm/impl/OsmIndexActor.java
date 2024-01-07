package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.executor.PlatformExecutor;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.data.EntityMapper;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxService;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.ConnectChunksMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OsmIndexActor {

    private final SearcherApi       searcherApi;
    private final BoxService        boxService;
    private final IDateTimeSupplier dateTimeSupplier;
    private final OsmIndexTask      task;
    private final PlatformExecutor  executor;

    @Logged("box")
    public void save(IndexBoxEntity box) {
        executor.submit(() -> task.accept(EntityMapper.MAPPER.indexBox(box)));
    }

    @Logged
    @Transactional
    public void connect(List<IndexBoxEntity> boxes) {
        var ids = ids(boxes);

        searcherApi.createConnections(new ConnectChunksMessage(ids));

        var timestamp = dateTimeSupplier.instant();

        boxService.boxes(ids).forEach(box -> box.setConnectionRequestTimestamp(timestamp));
    }

    private List<Integer> ids(List<IndexBoxEntity> boxes) {
        return boxes.stream().map(IndexBoxEntity::getId).toList();
    }

}
