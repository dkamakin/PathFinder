package com.github.pathfinder.indexer.service.osm.impl;

import java.util.List;
import static com.github.pathfinder.indexer.data.OsmMapper.MAPPER;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.osm.OsmConfigurationProperties;
import com.github.pathfinder.indexer.data.EntityMapper;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.exception.IndexBoxNotFoundException;
import com.github.pathfinder.indexer.service.Indexer;
import com.github.pathfinder.indexer.service.impl.IndexBoxSearcherService;
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

    private final OsmClient                  client;
    private final OsmPointExtractor          pointExtractor;
    private final SearcherApi                searcherApi;
    private final IndexBoxSearcherService    searcherService;
    private final OsmConfigurationProperties osmConfigurationProperties;

    @Override
    @Logged("box")
    public void process(int boxId) {
        var box = box(boxId);

        if (box.isSaved()) {
            log.warn("Box {} is already saved", box);
            return;
        }

        var elements = elements(box);
        var points   = pointExtractor.points(elements);

        searcherApi.save(request(box, points));
    }

    private List<OsmElement> elements(IndexBoxEntity box) {
        return client.elements(EntityMapper.MAPPER.osmBox(box),
                               MAPPER.osmQueryTags(osmConfigurationProperties.getTags()));
    }

    private IndexBoxEntity box(int boxId) {
        return searcherService.box(boxId).orElseThrow(() -> new IndexBoxNotFoundException(boxId));
    }

    private SavePointsMessage request(IndexBoxEntity entity, List<Point> points) {
        return new SavePointsMessage(new IndexBox(entity.getId(),
                                                  EntityMapper.MAPPER.coordinate(entity.getMin()),
                                                  EntityMapper.MAPPER.coordinate(entity.getMax())),
                                     points);
    }

}
