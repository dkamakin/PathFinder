package com.github.pathfinder.configuration;

import com.github.pathfinder.database.repository.impl.ProjectionRepository;
import com.github.pathfinder.service.impl.ChunkService;
import com.github.pathfinder.service.impl.DefaultProjectionService;
import com.github.pathfinder.service.impl.PointConnector;
import com.github.pathfinder.service.impl.PointService;
import com.github.pathfinder.service.impl.ProjectionService;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@DataNeo4jTest
@Target(ElementType.TYPE)
@ExtendWith(Neo4jExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Transactional(propagation = NOT_SUPPORTED)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@Import({Neo4jTestConfiguration.class,
        CoordinateConfiguration.class,
        Neo4jTestTemplate.class,
        ProjectionService.class,
        PointConnector.class,
        ProjectionRepository.class,
        DefaultProjectionService.class,
        PointService.class,
        ChunkService.class})
public @interface SearcherNeo4jTest {

}
