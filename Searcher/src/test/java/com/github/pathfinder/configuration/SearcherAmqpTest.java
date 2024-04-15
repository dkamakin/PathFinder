package com.github.pathfinder.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.pathfinder.listener.ConnectionsQueueListener;
import com.github.pathfinder.listener.DeadLetterListener;
import com.github.pathfinder.listener.DefaultQueueListener;
import com.github.pathfinder.listener.SaveChunksQueueListener;
import com.github.pathfinder.messaging.error.RethrowingToSenderErrorHandler;
import com.github.pathfinder.messaging.test.RabbitIntegrationTest;
import com.github.pathfinder.searcher.api.configuration.SearcherApiConfiguration;
import org.springframework.context.annotation.Import;

@RabbitIntegrationTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({SearcherApiConfiguration.class,
        SearcherAmqpTestConfiguration.class,
        DefaultQueueListener.class,
        ConnectionsQueueListener.class,
        SaveChunksQueueListener.class,
        RethrowingToSenderErrorHandler.class,
        DeadLetterListener.class})
public @interface SearcherAmqpTest {

}
