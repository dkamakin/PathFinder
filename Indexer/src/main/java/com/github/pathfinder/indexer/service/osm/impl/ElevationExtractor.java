package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.client.elevation.ElevationClient;
import com.github.pathfinder.indexer.data.elevation.Elevation;
import com.github.pathfinder.indexer.data.elevation.ElevationMapper;
import com.github.pathfinder.indexer.data.osm.OsmBoxIndex;
import com.github.pathfinder.indexer.data.osm.OsmExtendedBoxIndex;
import com.github.pathfinder.indexer.data.osm.OsmExtendedNode;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElevationExtractor {

    private final ElevationClient elevationClient;

    @Logged(ignoreReturnValue = false)
    public OsmExtendedBoxIndex extend(OsmBoxIndex index) {
        var nodes      = index.nodes();
        var elevations = fetchElevations(nodes);
        var minSize    = Math.min(elevations.size(), nodes.size());
        var result     = Lists.<OsmExtendedNode>newArrayListWithCapacity(minSize);

        log.info("Fetched {} elevations", elevations.size());

        for (var i = 0; i < minSize; i++) {
            var node      = nodes.get(i);
            var elevation = elevations.get(i);

            result.add(new OsmExtendedNode(elevation, node));
        }

        return new OsmExtendedBoxIndex(result, index.reverseWayIndex());
    }

    private List<Elevation> fetchElevations(List<OsmNode> nodes) {
        return elevationClient.elevations(nodes.stream()
                                                  .map(OsmNode::coordinate)
                                                  .map(ElevationMapper.MAPPER::elevationCoordinate)
                                                  .toList());
    }

}
