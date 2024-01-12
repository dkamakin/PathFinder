package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.data.OsmMapper;
import com.github.pathfinder.indexer.data.index.IndexBox;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxService;
import com.github.pathfinder.indexer.service.osm.IOsmIndexer;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.point.Point;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class OsmIndexer implements IOsmIndexer {

    private final OsmClient         client;
    private final OsmPointExtractor pointExtractor;
    private final SearcherApi       searcherApi;
    private final BoxService        boxService;
    private final IDateTimeSupplier dateTimeSupplier;

    @Override
    @Logged("box")
    @Transactional
    public void process(IndexBox box) {
        var elements = client.elements(OsmMapper.INSTANCE.osmBox(box));

        log.info("Query result: {} elements", elements.size());

        if (CollectionUtils.isEmpty(elements)) {
            return;
        }

        var points = pointExtractor.points(elements);
        boxService.box(box.id()).ifPresent(entity -> save(entity, points));
    }

    private void save(IndexBoxEntity box, List<Point> points) {
        searcherApi.save(new SavePointsMessage(box.getId(), points));
        box.setSaveRequestTimestamp(dateTimeSupplier.now());
    }

}
