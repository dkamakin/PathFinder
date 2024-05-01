package com.github.pathfinder.indexer.service.osm.impl;

import java.util.Collection;
import java.util.List;
import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.data.Distance;
import com.github.pathfinder.core.data.MetersDistance;
import com.github.pathfinder.core.tools.impl.GeodeticTools;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.data.OsmMapper;
import com.github.pathfinder.indexer.service.BoundingBoxSplitter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoundingBoxOsmElementsSplitter implements BoundingBoxSplitter {

    private static final int AZIMUTH_TO_EAST  = 90;
    private static final int AZIMUTH_TO_SOUTH = 180;

    private final GeodeticTools tools;
    private final long          elementsLimit;
    private final Distance      additionalSpace;
    private final OsmClient     osmClient;

    public BoundingBoxOsmElementsSplitter(long elementsLimit, Distance additionalSpace, OsmClient osmClient) {
        this.elementsLimit   = elementsLimit;
        this.additionalSpace = additionalSpace;
        this.osmClient       = osmClient;
        this.tools           = new GeodeticTools();
    }

    @Override
    public List<BoundingBox> split(BoundingBox box) {
        var countElements = countElements(box);

        if (countElements == 0) {
            log.info("A box does not contain elements");
            return List.of();
        }

        if (countElements <= elementsLimit) {
            log.info("A box {} size fits into the elements limits", box);
            return List.of(box);
        }

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

    private long countElements(BoundingBox box) {
        return osmClient.countElements(OsmMapper.MAPPER.osmBox(box));
    }

}
