package com.github.pathfinder.messaging;

import com.github.pathfinder.messaging.configuration.MessagingConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Inherited
@SpringBootTest
@Target(ElementType.TYPE)
@ExtendWith(RabbitExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Import(MessagingConfiguration.class)
public @interface RabbitIntegrationTest {

}

