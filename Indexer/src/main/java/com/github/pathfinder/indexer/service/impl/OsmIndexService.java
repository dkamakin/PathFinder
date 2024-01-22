package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.configuration.IndexerConfiguration;
import com.github.pathfinder.indexer.service.IActualizeService;
import com.github.pathfinder.indexer.service.IndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
        actualizeService.perform();
        actor.perform();
    }

}
