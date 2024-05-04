package com.github.pathfinder.indexer.service.osm.impl;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import com.github.pathfinder.indexer.data.osm.OsmExtendedBoxIndex;
import com.github.pathfinder.indexer.data.osm.OsmExtendedNode;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OsmLandTypeService {

    private final OsmLandTypeExtractor landTypeExtractor;

    public Optional<OsmLandType> landTypeWithMaxWeight(OsmExtendedNode extendedNode, OsmExtendedBoxIndex index) {
        var wayLandType  = landTypeWithMaxWeightFromWays(extendedNode, index);
        var nodeLandType = landTypeExtractor.from(extendedNode.node().tags());

        return Stream.of(wayLandType, nodeLandType)
                .flatMap(Optional::stream)
                .max(Comparator.comparing(OsmLandType::weight));
    }

    private Optional<OsmLandType> landTypeWithMaxWeightFromWays(OsmExtendedNode extendedNode, OsmExtendedBoxIndex index) {
        return index.ways(extendedNode).stream()
                .map(OsmWay::tags)
                .map(landTypeExtractor::from)
                .flatMap(Optional::stream)
                .max(Comparator.comparing(OsmLandType::weight));
    }

}
