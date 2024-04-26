package com.github.pathfinder.indexer.service.osm.impl;

import java.util.function.Predicate;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import lombok.ToString;

@ToString
public class OsmElementFilter implements Predicate<OsmElement> {

    private int notSupportedCount;

    @Override
    public boolean test(OsmElement element) {
        var isSupported = OsmLandType.from(element.tags()).map(OsmLandType::coefficient).isPresent();

        if (Boolean.FALSE.equals(isSupported)) {
            notSupportedCount++;
        }

        return isSupported;
    }

}
