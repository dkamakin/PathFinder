package com.github.pathfinder.indexer.configuration.osm;

import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.client.osm.westnordost.WestNordOstOsmClient;
import de.westnordost.osmapi.OsmConnection;
import de.westnordost.osmapi.overpass.OverpassMapDataApi;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Slf4j
@Validated
@RefreshScope
@Configuration
public class OsmClientConfiguration {

    @NotBlank
    @Value("${osm.client.overpass.url}")
    private String overpassUrl;

    @Bean
    public OsmClient osmClient() {
        log.info("Building an osm client with configuration: {}", this);
        var overpassConnection = new OsmConnection(overpassUrl, null);

        return new WestNordOstOsmClient(new OverpassMapDataApi(overpassConnection));
    }

}
