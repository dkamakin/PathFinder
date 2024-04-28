package com.github.pathfinder.indexer.service.osm.impl;

import java.util.List;
import java.util.Optional;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmExtendedNode;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import com.github.pathfinder.indexer.data.osm.PointCreationContext;
import com.github.pathfinder.searcher.api.data.point.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OsmPointExtractor {

    private final OsmElementsIndexer osmElementsIndexer;
    private final ElevationExtractor elevationExtractor;
    private final OsmLandTypeService landTypeService;

    public List<Point> points(List<OsmElement> elements) {
        var index   = osmElementsIndexer.index(elements);
        var context = new PointCreationContext(elevationExtractor.extend(index), new PointCreatorStatistics());

        return points(context);
    }

    public List<Point> points(PointCreationContext context) {
        var result = context.index().nodes().stream()
                .map(node -> createPointRequest(context, node))
                .flatMap(Optional::stream)
                .toList();

        log.info("Created {} points, statistics: {}", result.size(), context.statistics());

        return result;
    }

    private Optional<Point> createPointRequest(PointCreationContext context, OsmExtendedNode extendedNode) {
        if (extendedNode.elevation().value() == 0D) {
            log.warn("An elevation for the node {} is at the sea level", extendedNode);
            context.statistics().addUnknownElevationNode();
            return Optional.empty();
        }

        return landTypeService.landTypeWithMaxWeight(extendedNode, context.index())
                .map(landType -> createPointRequest(extendedNode, landType));
    }

    private Point createPointRequest(OsmExtendedNode extendedNode, OsmLandType landType) {
        return new Point(extendedNode.elevation().value(),
                         extendedNode.node().coordinate(),
                         landType.name(),
                         landType.weight());
    }

}
