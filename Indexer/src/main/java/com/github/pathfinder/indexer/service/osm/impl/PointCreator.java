package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.indexer.data.osm.OsmExtendedBoxIndex;
import com.github.pathfinder.indexer.data.osm.OsmExtendedNode;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import com.github.pathfinder.searcher.api.data.point.Point;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PointCreator {

    private final OsmExtendedBoxIndex    index;
    private final PointCreatorStatistics statistics;

    public PointCreator(OsmExtendedBoxIndex index) {
        this.index      = index;
        this.statistics = new PointCreatorStatistics();
    }

    public List<Point> points() {
        var result = index.nodes().stream()
                .map(this::createPointRequest)
                .flatMap(Optional::stream)
                .toList();

        log.info("Created {} points, statistics: {}", result.size(), statistics);

        return result;
    }

    private Optional<Point> createPointRequest(OsmExtendedNode extendedNode) {
        var landType = OsmLandTypeExtractor.maxLandType(extendedNode, index).orElseGet(this::unknownType);

        if (extendedNode.elevation().value() == 0D) {
            log.warn("An elevation for the node {} is at the sea level", extendedNode);
            statistics.addUnknownElevationNode();
            return Optional.empty();
        }

        return Optional.of(
                new Point(extendedNode.elevation().value(),
                          extendedNode.node().coordinate(),
                          landType.name(),
                          landType.coefficient())
        );
    }

    private OsmLandType unknownType() {
        statistics.addUnknownTypeNode();
        return OsmLandType.UNKNOWN;
    }

}
