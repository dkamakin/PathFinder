package com.github.pathfinder.security.configuration;

import com.github.pathfinder.messaging.RabbitIntegrationTest;
import com.github.pathfinder.security.api.configuration.SecurityApiConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Import;

@RabbitIntegrationTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@Import(SecurityApiConfiguration.class)
public @interface SecurityAmqpTest {

}
