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

    private final OsmElementsIndexer osmElementsIndexer;
    private final ElevationExtractor elevationExtractor;

    public List<Point> points(List<OsmElement> elements) {
        var index    = osmElementsIndexer.index(elements);
        var extended = elevationExtractor.extend(index);

        return new PointCreator(extended).points();
    }

}
