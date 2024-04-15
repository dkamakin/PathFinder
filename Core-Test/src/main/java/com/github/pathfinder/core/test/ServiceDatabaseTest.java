package com.github.pathfinder.core.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Transactional(propagation = NOT_SUPPORTED)
public @interface ServiceDatabaseTest {

}
