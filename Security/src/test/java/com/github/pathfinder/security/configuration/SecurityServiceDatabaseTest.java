package com.github.pathfinder.security.configuration;

import com.github.pathfinder.core.ServiceDatabaseTest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Inherited
@ServiceDatabaseTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({SecurityTestDatabaseTemplate.class})
public @interface SecurityServiceDatabaseTest {

}
