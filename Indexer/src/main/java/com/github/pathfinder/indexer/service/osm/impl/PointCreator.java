package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.indexer.data.osm.OsmExtendedNode;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import com.github.pathfinder.searcher.api.data.point.Point;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PointCreator {

    private final List<OsmExtendedNode>  nodes;
    private final PointCreatorStatistics statistics;

    public PointCreator(List<OsmExtendedNode> nodes) {
        this.nodes      = nodes;
        this.statistics = new PointCreatorStatistics();
    }

    public List<Point> points() {
        var result = nodes.stream()
                .map(this::createPointRequest)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        log.info("Created {} points, statistics: {}", result.size(), statistics);

        return result;
    }

    private Optional<Point> createPointRequest(OsmExtendedNode extendedNode) {
        var landType = landType(extendedNode);

        if (extendedNode.elevation().elevation() == 0D) {
            log.warn("An elevation for the node {} is at the sea level", extendedNode);
            statistics.addUnknownElevationNode();
            return Optional.empty();
        }

        return Optional.of(
                new Point(extendedNode.elevation().elevation(),
                          extendedNode.node().coordinate(),
                          landType.name(),
                          landType.coefficient())
        );
    }

    private OsmLandType landType(OsmExtendedNode extendedNode) {
        return OsmLandType.from(extendedNode.node().tags()).orElseGet(this::unknownType);
    }

    private OsmLandType unknownType() {
        statistics.addUnknownTypeNode();
        return OsmLandType.UNKNOWN;
    }

}
