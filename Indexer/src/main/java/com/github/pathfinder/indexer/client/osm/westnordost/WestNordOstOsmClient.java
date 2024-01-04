package com.github.pathfinder.indexer.client.osm.westnordost;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.client.osm.impl.OverpassQueryBuilder;
import com.github.pathfinder.indexer.configuration.osm.IndexBox;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.exception.ApiExecutionException;
import de.westnordost.osmapi.OsmConnection;
import de.westnordost.osmapi.map.MapDataApi;
import de.westnordost.osmapi.overpass.OverpassMapDataApi;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WestNordOstOsmClient implements OsmClient {

    private final OverpassMapDataApi overpassApi;
    private final MapDataApi         mapDataApi;

    public WestNordOstOsmClient(OsmConnection connection) {
        this.overpassApi = new OverpassMapDataApi(connection);
        this.mapDataApi  = new MapDataApi(connection);
    }

    @Logged
    @Override
    public List<OsmNode> elements(List<Long> ids) {
        return mapDataApi.getNodes(ids).stream().map(WestNordOstMapper.MAPPER::osmNode).toList();
    }

    @Logged
    @Override
    public List<OsmElement> queryElements(String overpassQuery) {
        var handler = new ListCollectingDataHandler();

        overpassHandle(api -> api.queryElements(overpassQuery, handler));

        log.info("Executed an overpass action, total elements: {}", handler.getElements().size());

        return handler.getElements();
    }

    @Logged
    @Override
    public List<OsmElement> queryElements(IndexBox box) {
        return queryElements(new OverpassQueryBuilder("""
                                                              (
                                                              node($min,$max);
                                                              way($min,$max);
                                                              );
                                                              out body;
                                                              """
        ).bind(Map.of(
                "min", box.getMin(),
                "max", box.getMax()
        )));
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
            return action.apply(overpassApi);
        } catch (Exception e) {
            log.error("Failed to execute an overpass action", e);
            throw new ApiExecutionException("Failed to execute an overpass action");
        }
    }

}
