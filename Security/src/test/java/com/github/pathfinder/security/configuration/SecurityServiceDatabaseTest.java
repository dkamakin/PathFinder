package com.github.pathfinder.security.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.pathfinder.core.test.ServiceDatabaseTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;

@ServiceDatabaseTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SecurityDatabaseTestExtension.class)
@Import({SecurityTestDatabaseTemplate.class})
public @interface SecurityServiceDatabaseTest {

}
