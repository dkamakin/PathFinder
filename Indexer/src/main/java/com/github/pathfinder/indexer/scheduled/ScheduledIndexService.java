package com.github.pathfinder.indexer.scheduled;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.configuration.IndexerConfiguration;
import com.github.pathfinder.indexer.service.IActualizeService;
import com.github.pathfinder.indexer.service.impl.IndexActor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledIndexService {

    private final IActualizeService actualizeService;
    private final IndexActor        actor;

    @Logged
    @Scheduled(fixedDelayString = IndexerConfiguration.INDEX_DELAY)
    public void run() {
        actualizeService.perform();
        actor.perform();
    }

}
