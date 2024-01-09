package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.searcher.api.data.point.Point;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OsmPointExtractor {

    private final NodeExtractor      nodeExtractor;
    private final ElevationExtractor elevationExtractor;

    public List<Point> points(List<OsmElement> elements) {
        var nodes = nodeExtractor.nodes(elements);

        log.info("Extracted {} nodes from {} elements", nodes.size(), elements.size());

        var extendedNodes = elevationExtractor.extend(nodes);

        log.info("Fetched {} elevations", extendedNodes.size());

        return new PointCreator(extendedNodes).points();
    }

}
