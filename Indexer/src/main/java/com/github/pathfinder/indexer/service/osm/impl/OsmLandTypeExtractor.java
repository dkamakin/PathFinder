package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.indexer.data.osm.OsmExtendedBoxIndex;
import com.github.pathfinder.indexer.data.osm.OsmExtendedNode;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OsmLandTypeExtractor {

    public static Optional<OsmLandType> maxLandType(OsmExtendedNode extendedNode, OsmExtendedBoxIndex index) {
        var wayLandType  = landTypeFromWay(extendedNode, index);
        var nodeLandType = OsmLandType.from(extendedNode.node().tags());

        return Stream.of(wayLandType, nodeLandType)
                .flatMap(Optional::stream)
                .max(Comparator.comparing(OsmLandType::coefficient));
    }

    private Optional<OsmLandType> landTypeFromWay(OsmExtendedNode extendedNode, OsmExtendedBoxIndex index) {
        return findMaxLandType(index.ways(extendedNode));
    }

    private Optional<OsmLandType> findMaxLandType(List<OsmWay> ways) {
        return ways.stream()
                .map(OsmWay::tags)
                .map(OsmLandType::from)
                .flatMap(Optional::stream)
                .max(Comparator.comparing(OsmLandType::coefficient));
    }

}
