package com.github.pathfinder.indexer.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxSearcherService;
import com.github.pathfinder.indexer.service.IActualizeService;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.Chunk;
import com.github.pathfinder.searcher.api.data.GetChunksMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActualizeService implements IActualizeService {

    private final SearcherApi        searcherApi;
    private final BoxSearcherService boxSearcherService;

    @Override
    @Transactional
    @Logged(ignoreReturnValue = false)
    public void perform() {
        var boxes = boxSearcherService.all();

        if (CollectionUtils.isEmpty(boxes)) {
            log.info("Nothing to actualize");
            return;
        }

        var chunks        = searcherApi.chunks(getChunksMessage(boxes)).chunks();
        var indexedChunks = indexChunks(chunks);

        boxes.forEach(box -> actualize(box, indexedChunks));
    }

    private void actualize(IndexBoxEntity box, Map<Integer, Chunk> chunks) {
        Optional.ofNullable(chunks.get(box.getId()))
                .ifPresentOrElse(chunk -> handle(box, chunk), () -> handleNotFound(box));
    }

    private void handleNotFound(IndexBoxEntity box) {
        if (box.isSaved()) {
            log.warn("Box was set as saved, but not found in the last try");
            box.setSaved(false);
        }

        if (box.isConnected()) {
            log.warn("Box was set as connected, but not found in the last try");
            box.setConnected(false);
        }
    }

    private void handle(IndexBoxEntity box, Chunk chunk) {
        if (!box.isSaved()) {
            log.info("Detected a change: the box {} was saved", box.getId());
            box.setSaved(true);
        }

        if (box.isConnected() != chunk.connected()) {
            log.info("Detected a change: the box {} connection status has changed from: {} to: {}",
                     box.getId(), box.isConnected(), chunk.connected());
            box.setConnected(chunk.connected());
        }
    }

    private Map<Integer, Chunk> indexChunks(List<Chunk> boxes) {
        return boxes.stream().collect(Collectors.toMap(Chunk::id, Function.identity()));
    }

    private GetChunksMessage getChunksMessage(List<IndexBoxEntity> boxes) {
        return new GetChunksMessage(ids(boxes));
    }

    private List<Integer> ids(List<IndexBoxEntity> boxes) {
        return boxes.stream().map(IndexBoxEntity::getId).toList();
    }

}
