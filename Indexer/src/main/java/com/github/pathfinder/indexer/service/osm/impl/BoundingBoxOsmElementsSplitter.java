package com.github.pathfinder.indexer.service.osm.impl;

import java.util.Collection;
import java.util.List;
import static com.github.pathfinder.indexer.data.OsmMapper.MAPPER;
import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.data.Distance;
import com.github.pathfinder.core.data.MetersDistance;
import com.github.pathfinder.core.tools.impl.GeodeticTools;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmElementType;
import com.github.pathfinder.indexer.data.osm.OsmQueryTag;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import com.github.pathfinder.indexer.service.BoundingBoxSplitter;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class BoundingBoxOsmElementsSplitter implements BoundingBoxSplitter {

    private static final int AZIMUTH_TO_EAST  = 90;
    private static final int AZIMUTH_TO_SOUTH = 180;

    @Builder.Default
    private final GeodeticTools     tools = new GeodeticTools();
    private final long              elementsLimit;
    private final Distance          additionalSpace;
    private final OsmClient         osmClient;
    private final List<OsmQueryTag> tags;

    @Override
    public List<BoundingBox> split(BoundingBox box) {
        log.info("Working with the box: {}", box);

        var osmBox     = MAPPER.osmBox(box);
        var nodesCount = osmClient.count(OsmElementType.NODE, osmBox, tags);

        log.info("Nodes count: {}", nodesCount);

        if (nodesCount >= elementsLimit) {
            return performSplit(box);
        }

        var waysCount = osmClient.count(OsmElementType.WAY, osmBox, tags);

        log.info("Ways count: {}", waysCount);

        if (sumElements(nodesCount, waysCount) >= elementsLimit) {
            return performSplit(box);
        }

        var ways  = osmClient.ways(osmBox, tags);
        var total = countNodes(ways) + nodesCount;

        log.info("The total elements count: {}", total);

        if (total == 0) {
            log.info("The box does not contain elements");
            return List.of();
        }

        if (total <= elementsLimit) {
            log.info("The box fits into the limit");
            return List.of(box);
        }

        return performSplit(box);
    }

    /**
     * A way can have between 2 and 2,000 nodes, although it's possible that faulty ways with zero or a single node exist.
     */
    private long sumElements(long nodesCount, long waysCount) {
        return nodesCount + waysCount * 2;
    }

    private List<BoundingBox> performSplit(BoundingBox box) {
        log.info("Performing split of the box");

        var width  = tools.width(box);
        var height = tools.height(box);

        return splitBox(box, width, height).stream()
                .map(this::split)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<BoundingBox> splitBox(BoundingBox box, Distance width, Distance height) {
        if (width.compareTo(height) > 0) {
            return splitByWidth(box, width);
        } else {
            return splitByHeight(box, height);
        }
    }

    private List<BoundingBox> splitByHeight(BoundingBox box, Distance height) {
        var sourceCoordinate    = new Coordinate(box.max().latitude(), box.min().longitude());
        var halfPointCoordinate = tools.move(sourceCoordinate, AZIMUTH_TO_SOUTH, distanceToMove(height));

        return List.of(
                new BoundingBox(box.min(),
                                new Coordinate(halfPointCoordinate.latitude(), box.max().longitude())),
                new BoundingBox(new Coordinate(halfPointCoordinate.latitude(), box.min().longitude()),
                                box.max())
        );
    }

    private List<BoundingBox> splitByWidth(BoundingBox box, Distance width) {
        var halfPointCoordinate = tools.move(box.min(), AZIMUTH_TO_EAST, distanceToMove(width));

        return List.of(
                new BoundingBox(box.min(),
                                new Coordinate(box.max().latitude(), halfPointCoordinate.longitude())),
                new BoundingBox(halfPointCoordinate,
                                box.max())
        );
    }

    private Distance distanceToMove(Distance distance) {
        return new MetersDistance(distance.meters() / 2 + additionalSpace.meters());
    }

    private int countNodes(List<OsmElement> ways) {
        return ways.stream()
                .filter(element -> element.type() == OsmElementType.WAY)
                .map(OsmElement::asWay)
                .map(OsmWay::nodeIds)
                .map(List::size)
                .reduce(0, Integer::sum);
    }

}
