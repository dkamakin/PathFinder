package com.github.pathfinder.indexer.service.osm.impl;

import java.util.List;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.data.EntityMapper;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxUpdaterService;
import com.github.pathfinder.indexer.service.Indexer;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.IndexBox;
import com.github.pathfinder.searcher.api.data.point.Point;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class OsmIndexer implements Indexer {

    private final OsmClient         client;
    private final OsmPointExtractor pointExtractor;
    private final SearcherApi       searcherApi;
    private final IDateTimeSupplier dateTimeSupplier;
    private final BoxUpdaterService boxUpdaterService;

    @Override
    @Logged("box")
    public void process(IndexBoxEntity box) {
        var elements = client.elements(EntityMapper.MAPPER.osmBox(box));
        var points   = pointExtractor.points(elements);

        searcherApi.save(request(box, points));
        boxUpdaterService.save(box.setSaveRequestTimestamp(dateTimeSupplier.now()));
    }

    private SavePointsMessage request(IndexBoxEntity entity, List<Point> points) {
        return new SavePointsMessage(new IndexBox(entity.getId(),
                                                  EntityMapper.MAPPER.coordinate(entity.getMin()),
                                                  EntityMapper.MAPPER.coordinate(entity.getMax())),
                                     points);
    }

}
