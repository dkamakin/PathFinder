package com.github.pathfinder.configuration;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.core.web.configuration.impl.CoreWebConfiguration;
import com.github.pathfinder.security.api.configuration.SecurityApiConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        CoreConfiguration.class,
        SecurityApiConfiguration.class,
        CoreWebConfiguration.class,
        Neo4jConfiguration.class,
})
public class SearcherConfiguration {

}
