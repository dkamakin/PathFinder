package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import lombok.ToString;

@ToString
public class OsmElementFilter {

    private int notSupportedCount;

    public boolean isSupported(OsmElement element) {
        var isSupported = OsmLandType.from(element.tags())
                .map(OsmLandType::coefficient)
                .map(coefficient -> coefficient > 0)
                .orElse(true);

        if (Boolean.FALSE.equals(isSupported)) {
            notSupportedCount++;
        }

        return isSupported;
    }

}
