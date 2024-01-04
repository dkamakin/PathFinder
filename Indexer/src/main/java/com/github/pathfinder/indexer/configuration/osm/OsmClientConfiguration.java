package com.github.pathfinder.indexer.configuration.osm;

import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.client.osm.westnordost.WestNordOstOsmClient;
import de.westnordost.osmapi.OsmConnection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Slf4j
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "osm.client")
public class OsmClientConfiguration {

    @NotBlank
    private String url;

    @Bean
    public OsmClient osmClient() {
        log.info("Building an osm client with configuration: {}", this);
        return new WestNordOstOsmClient(new OsmConnection(url, null));
    }

}
