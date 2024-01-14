package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import java.util.function.Predicate;
import lombok.ToString;

@ToString
public class OsmElementFilter implements Predicate<OsmElement> {

    private int notSupportedCount;

    @Override
    public boolean test(OsmElement element) {
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
