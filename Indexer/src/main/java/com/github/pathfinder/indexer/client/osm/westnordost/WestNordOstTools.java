package com.github.pathfinder.indexer.client.osm.westnordost;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import com.github.pathfinder.core.exception.InternalServerException;
import com.github.pathfinder.indexer.data.osm.OsmElementType;
import de.westnordost.osmapi.overpass.ElementCount;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WestNordOstTools {

    private static final Map<OsmElementType, Function<ElementCount, Long>> osmCountExtractors = Map.of(
            OsmElementType.WAY, x -> x.ways,
            OsmElementType.NODE, x -> x.nodes
    );

    public long extractElementsCount(OsmElementType elementType, ElementCount elementCount) {
        return Optional.ofNullable(osmCountExtractors.get(elementType))
                .map(x -> x.apply(elementCount))
                .orElseThrow(() -> new InternalServerException("Unsupported element type: " + elementType));
    }

}
