package com.github.pathfinder.indexer;

import com.github.pathfinder.core.ServiceDatabaseTest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;

@Inherited
@ServiceDatabaseTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(IndexerDatabaseTestExtension.class)
@Import({IndexerTestDatabaseTemplate.class})
public @interface IndexerServiceDatabaseTest {

}
