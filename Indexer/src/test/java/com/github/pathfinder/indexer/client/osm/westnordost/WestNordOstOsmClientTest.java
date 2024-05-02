package com.github.pathfinder.indexer.client.osm.westnordost;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.IndexerOsmTestConfiguration;
import com.github.pathfinder.indexer.exception.ApiExecutionException;
import de.westnordost.osmapi.overpass.OverpassMapDataApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@IndexerOsmTestConfiguration
@Import(WestNordOstOsmClient.class)
class WestNordOstOsmClientTest {

    @MockBean
    OverpassMapDataApi overpassApi;

    @Autowired
    OsmClient target;

    void whenNeedToThrowOnNodes(RuntimeException expected) {
        doThrow(expected).when(overpassApi).queryElements(any(), any());
    }

    @Test
    void nodes_ExceptionOccurred_RetryException() {
        whenNeedToThrowOnNodes(new ApiExecutionException("test"));

        var request = List.of(1L);

        assertThatThrownBy(() -> target.nodes(request)).isInstanceOf(ApiExecutionException.class);

        verify(overpassApi, times(5)).queryElements(any(), any());
    }

}