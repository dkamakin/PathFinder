package com.github.pathfinder.indexer.configuration.osm;

import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.client.osm.westnordost.WestNordOstOsmClient;
import de.westnordost.osmapi.OsmConnection;
import de.westnordost.osmapi.overpass.OverpassMapDataApi;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Data
@Validated
@RefreshScope
@Configuration
public class OsmConfiguration {

    @Bean
    public OsmConnection osmConnection(OsmConfigurationProperties configurationProperties) {
        log.info("OSM Client configuration: {}", configurationProperties.getClient());

        return new OsmConnection(configurationProperties.getClient().overpassUrl(), null);
    }

    @Bean
    public OverpassMapDataApi overpassMapDataApi(OsmConnection osmConnection) {
        return new OverpassMapDataApi(osmConnection);
    }

    @Bean
    public OsmClient osmClient(OverpassMapDataApi overpassMapDataApi) {
        return new WestNordOstOsmClient(overpassMapDataApi);
    }

}
