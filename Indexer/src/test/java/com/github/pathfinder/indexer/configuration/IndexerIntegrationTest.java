package com.github.pathfinder.indexer.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.pathfinder.core.configuration.CoreConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {
        CoreConfiguration.class,
        IndexerConfiguration.class,
        RefreshAutoConfiguration.class,
        LocalValidatorFactoryBean.class,
})
public @interface IndexerIntegrationTest {
}
