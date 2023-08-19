package com.github.pathfinder.configuration;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({CoreConfiguration.class})
public class SearcherWebTestConfiguration {

}
