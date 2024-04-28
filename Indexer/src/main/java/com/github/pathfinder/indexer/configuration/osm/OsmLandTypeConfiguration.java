package com.github.pathfinder.indexer.configuration.osm;

import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@RefreshScope
@ConfigurationProperties(value = "osm.land-types", ignoreUnknownFields = false)
public class OsmLandTypeConfiguration {

    @NotEmpty
    private Set<String> keys;

    @NotEmpty
    private Set<OsmTagConfiguration> values;

    public record OsmTagConfiguration(@NotBlank String name, double weight) {
    }

}
