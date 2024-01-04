package com.github.pathfinder.indexer.configuration.osm;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "osm.index")
public class OsmIndexConfiguration {

    @NotEmpty
    private List<@Valid IndexBox> boxes;

}
