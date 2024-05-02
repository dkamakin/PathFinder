package com.github.pathfinder.indexer.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.pathfinder.indexer.client.osm.westnordost.WestNordOstOsmClient;
import com.github.pathfinder.indexer.configuration.osm.OsmConfiguration;
import org.springframework.context.annotation.Import;

@IndexerIntegrationTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        OsmTestTemplate.class,
        WestNordOstOsmClient.class,
        OsmConfiguration.class,
})
public @interface IndexerOsmTestConfiguration {
}
