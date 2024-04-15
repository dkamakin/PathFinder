package com.github.pathfinder.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;
import com.github.pathfinder.service.impl.ChunkUpdaterService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataNeo4jTest
@Target(ElementType.TYPE)
@ExtendWith(Neo4jExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Transactional(propagation = NOT_SUPPORTED)
@Import({Neo4jTestConfiguration.class,
        CoordinateConfiguration.class,
        Neo4jTestTemplate.class,
        ChunkUpdaterService.class})
public @interface SearcherNeo4jTest {

}
