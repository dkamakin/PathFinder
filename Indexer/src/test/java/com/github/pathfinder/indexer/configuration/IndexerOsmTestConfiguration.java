package com.github.pathfinder.indexer.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.pathfinder.indexer.configuration.osm.OsmLandTypeConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {
        OsmTestTemplate.class,
        OsmLandTypeConfiguration.class,
        RefreshAutoConfiguration.class
})
public @interface IndexerOsmTestConfiguration {
}
