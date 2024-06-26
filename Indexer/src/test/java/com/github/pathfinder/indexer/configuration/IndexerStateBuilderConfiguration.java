package com.github.pathfinder.indexer.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.pathfinder.indexer.service.impl.IndexBoxUpdaterService;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({IndexBoxUpdaterService.class,
        RegionTestTemplate.class,
        IndexerStateBuilder.class})
public @interface IndexerStateBuilderConfiguration {
}
