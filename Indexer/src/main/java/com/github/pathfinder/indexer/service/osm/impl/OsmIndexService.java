package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.executor.PlatformExecutor;
import com.github.pathfinder.indexer.configuration.osm.IndexBox;
import com.github.pathfinder.indexer.configuration.osm.OsmIndexConfiguration;
import com.github.pathfinder.indexer.service.IndexService;
import com.github.pathfinder.searcher.api.SearcherApi;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OsmIndexService implements IndexService {

    private final OsmIndexConfiguration configuration;
    private final SearcherApi           searcherApi;
    private final PlatformExecutor      executor;
    private final OsmIndexTask          task;

    @Async
    @Logged
    @Override
    @Scheduled
    public void run() {
        var tasks = configuration.getBoxes().stream().map(this::task).toList();

        executor.invokeAll(tasks);
        searcherApi.createConnections();
    }

    private Callable<Object> task(IndexBox box) {
        return Executors.callable(() -> task.accept(box));
    }

}
