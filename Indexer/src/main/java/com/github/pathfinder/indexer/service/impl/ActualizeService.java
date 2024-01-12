package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxService;
import com.github.pathfinder.indexer.service.IActualizeService;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.Chunk;
import com.github.pathfinder.searcher.api.data.GetChunksMessage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActualizeService implements IActualizeService {

    private final SearcherApi searcherApi;
    private final BoxService  boxService;

    @Override
    @Transactional
    @Logged(ignoreReturnValue = false)
    public void perform() {
        var notSavedOrConnectedBoxes = boxService.notSavedOrConnected();

        if (CollectionUtils.isEmpty(notSavedOrConnectedBoxes)) {
            log.info("Not saved or connected boxes not found");
            return;
        }

        var chunks        = searcherApi.chunks(getChunksMessage(notSavedOrConnectedBoxes)).chunks();
        var indexedChunks = indexChunks(chunks);

        notSavedOrConnectedBoxes.forEach(box -> actualize(box, indexedChunks));
    }

    private void actualize(IndexBoxEntity box, Map<Integer, Chunk> chunks) {
        Optional.ofNullable(chunks.get(box.getId()))
                .ifPresentOrElse(chunk -> actualize(box, chunk),
                                 () -> log.info("Box is not saved yet: {}", box));
    }

    private void actualize(IndexBoxEntity box, Chunk chunk) {
        log.info("Actualize {}, chunk: {}", box, chunk);

        if (!box.isSaved()) {
            log.info("Detected a change: the box was saved");
            box.saved();
        }

        if (box.isConnected() != chunk.connected()) {
            log.info("Detected a change: the box connection status has changed, current: {}. actual: {}",
                     box.isConnected(), chunk.connected());
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
