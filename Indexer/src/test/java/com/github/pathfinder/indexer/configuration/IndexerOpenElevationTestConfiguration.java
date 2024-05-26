package com.github.pathfinder.indexer.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.pathfinder.indexer.configuration.elevation.OpenElevationConfiguration;
import org.springframework.context.annotation.Import;

@IndexerIntegrationTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        HttpConfiguration.class,
        OpenElevationConfiguration.class
})
public @interface IndexerOpenElevationTestConfiguration {
}
