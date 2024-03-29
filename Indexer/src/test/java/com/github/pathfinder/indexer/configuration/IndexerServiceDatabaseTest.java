package com.github.pathfinder.indexer.configuration;

import com.github.pathfinder.core.ServiceDatabaseTest;
import com.github.pathfinder.core.configuration.CoreConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Import;

@ServiceDatabaseTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(IndexerDatabaseTestExtension.class)
@Import({IndexerTestDatabaseTemplate.class,
        RefreshAutoConfiguration.class,
        CoreConfiguration.class})
public @interface IndexerServiceDatabaseTest {

}
