package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.configuration.IndexerConfiguration;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.IActualizeService;
import com.github.pathfinder.indexer.service.IndexService;
import com.github.pathfinder.indexer.service.osm.impl.OsmIndexActor;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class OsmIndexService implements IndexService {

    private final IActualizeService actualizeService;
    private final OsmIndexActor     actor;

    @Logged
    @Override
    @Scheduled(fixedDelayString = IndexerConfiguration.INDEX_DELAY)
    public void run() {
        var notSavedOrConnected = actualizeService.perform();

        if (CollectionUtils.isEmpty(notSavedOrConnected)) {
            log.info("Not saved or connected boxes not found");
            return;
        }

        notSavedOrConnected.stream()
                .filter(Predicate.not(IndexBoxEntity::isSaved))
                .forEach(actor::save);

        if (isNeedToConnect(notSavedOrConnected)) {
            actor.connect(notSavedOrConnected);
        }
    }

    private boolean isNeedToConnect(List<IndexBoxEntity> boxes) {
        return boxes.stream().allMatch(IndexBoxEntity::isSaved);
    }

}
