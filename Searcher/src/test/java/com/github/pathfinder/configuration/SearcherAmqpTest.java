package com.github.pathfinder.configuration;

import com.github.pathfinder.listener.DeadLetterListener;
import com.github.pathfinder.listener.SearcherListener;
import com.github.pathfinder.messaging.RabbitIntegrationTest;
import com.github.pathfinder.messaging.error.RethrowingToSenderErrorHandler;
import com.github.pathfinder.searcher.api.configuration.SearcherApiConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Import;

@Inherited
@RabbitIntegrationTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@Import({SearcherApiConfiguration.class,
        SearcherListener.class,
        RethrowingToSenderErrorHandler.class,
        DeadLetterListener.class})
public @interface SearcherAmqpTest {

}
