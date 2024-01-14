package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.data.EntityMapper;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxUpdaterService;
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
    private final IDateTimeSupplier dateTimeSupplier;
    private final BoxUpdaterService boxUpdaterService;

    @Override
    @Logged("box")
    public void process(IndexBoxEntity box) {
        var elements = client.elements(EntityMapper.MAPPER.osmBox(box));

        log.info("Query result: {} elements", elements.size());

        if (CollectionUtils.isEmpty(elements)) {
            log.info("No points found for {}", box);
            return;
        }

        var points = pointExtractor.points(elements);

        searcherApi.save(new SavePointsMessage(box.getId(), points));
        boxUpdaterService.save(box.setSaveRequestTimestamp(dateTimeSupplier.now()));
    }

}
