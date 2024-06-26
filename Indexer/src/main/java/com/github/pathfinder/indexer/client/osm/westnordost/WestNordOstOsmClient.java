package com.github.pathfinder.indexer.client.osm.westnordost;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.client.osm.impl.OsmClientRetryable;
import com.github.pathfinder.indexer.client.osm.impl.OverpassQueryBuilder;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmElementType;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.data.osm.OsmQueryTag;
import com.github.pathfinder.indexer.exception.ApiExecutionException;
import de.westnordost.osmapi.overpass.OverpassMapDataApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
@OsmClientRetryable
@RequiredArgsConstructor
public class WestNordOstOsmClient implements OsmClient {

    private final OverpassMapDataApi overpassMapDataApi;

    @Logged
    @Override
    public List<OsmNode> nodes(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        return queryElements(new OverpassQueryBuilder().nodes(ids).asBody(), new NodesCollectingDataHandler());
    }

    @Override
    @Logged(value = "box")
    public List<OsmElement> elements(OsmBox box, List<OsmQueryTag> tags) {
        return queryElements(new OverpassQueryBuilder().node(box, tags).way(box, tags).asBody(),
                             new ListCollectingDataHandler());
    }

    @Override
    @Logged(value = "box")
    public List<OsmElement> ways(OsmBox box, List<OsmQueryTag> tags) {
        return queryElements(new OverpassQueryBuilder().way(box, tags).asBody(), new ListCollectingDataHandler());
    }

    @Override
    @Logged(ignoreReturnValue = false, value = "box")
    public long count(OsmElementType element, OsmBox box, List<OsmQueryTag> tags) {
        var query = new OverpassQueryBuilder().element(element, box, tags).asCount();
        var count = overpass(api -> api.queryCount(query));

        return WestNordOstTools.extractElementsCount(element, count);
    }

    private <T extends OsmElement> List<T> queryElements(String overpassQuery, WestNordOstHandler<T> handler) {
        overpassHandle(api -> api.queryElements(overpassQuery, handler));

        var result = handler.result();

        log.info("Executed an overpass action, total elements: {}", result.size());

        return result;
    }

    private void overpassHandle(Consumer<OverpassMapDataApi> action) {
        overpass(api -> {
            action.accept(api);
            return null;
        });
    }

    private <T> T overpass(Function<OverpassMapDataApi, T> action) {
        try {
            log.info("Executing an overpass API call");
            return action.apply(overpassMapDataApi);
        } catch (Exception e) {
            log.error("Failed to execute an overpass action", e);
            throw new ApiExecutionException("Failed to execute an overpass action");
        }
    }

}
