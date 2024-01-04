package com.github.pathfinder.indexer;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

@Inherited
@SpringBootTest(classes = {
        CoreConfiguration.class,
        RefreshAutoConfiguration.class
})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexerIntegrationTest {

}
