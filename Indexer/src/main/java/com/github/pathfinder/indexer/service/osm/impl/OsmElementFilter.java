package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OsmElementFilter {

    public static boolean isSupported(OsmElement element) {
        return OsmLandType.from(element.tags())
                .map(OsmLandType::coefficient)
                .map(coefficient -> coefficient > 0)
                .orElse(true);
    }

}
