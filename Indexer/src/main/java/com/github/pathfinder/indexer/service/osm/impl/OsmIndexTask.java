package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.indexer.data.index.IndexBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OsmIndexTask {

    private final OsmIndexer indexer;

    public void accept(IndexBox box) {
        try {
            indexer.process(box);
        } catch (Exception e) {
            log.error("An index task has failed", e);
        }
    }

}
