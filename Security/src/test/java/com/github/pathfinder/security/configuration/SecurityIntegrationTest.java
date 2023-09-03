package com.github.pathfinder.security.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TestPropertyConfiguration.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public @interface SecurityIntegrationTest {

}
