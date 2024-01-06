package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.osm.IndexBox;
import com.github.pathfinder.indexer.service.osm.IOsmIndexer;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class OsmIndexer implements IOsmIndexer {

    private final OsmClient         client;
    private final OsmPointExtractor pointExtractor;
    private final SearcherApi       searcherApi;

    @Override
    public void process(IndexBox box) {
        var elements = client.elements(box);

        log.info("Query result: {} elements", elements.size());

        if (CollectionUtils.isEmpty(elements)) {
            return;
        }

        var points = pointExtractor.points(elements);
        var saved = searcherApi.save(new SavePointsMessage(points));

        log.info("Saved {} points", saved);
    }

}
