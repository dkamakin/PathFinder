package com.github.pathfinder.indexer.configuration.osm;

import java.util.Set;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.client.osm.westnordost.WestNordOstOsmClient;
import de.westnordost.osmapi.OsmConnection;
import de.westnordost.osmapi.overpass.OverpassMapDataApi;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Data
@Validated
@RefreshScope
@ConfigurationProperties(value = "osm", ignoreUnknownFields = false)
public class OsmConfiguration {

    @NotEmpty
    private Set<@Valid OsmTagConfiguration> tags;

    @NotNull
    private OsmClientConfiguration client;

    public record OsmClientConfiguration(@NotBlank String overpassUrl) {
    }

    public record OsmTagConfiguration(@NotBlank String name, Set<OsmTagValue> values) {
    }

    public record OsmTagValue(@NotBlank String name, double weight) {
    }

    @Bean
    public OsmClient osmClient() {
        log.info("Building an osm client with configuration: {}", this);
        var overpassConnection = new OsmConnection(client.overpassUrl(), null);

        return new WestNordOstOsmClient(new OverpassMapDataApi(overpassConnection));
    }
}