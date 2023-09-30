package com.github.pathfinder.messaging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@Inherited
@SpringBootTest
@Target(ElementType.TYPE)
@ExtendWith(RabbitExtension.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface RabbitIntegrationTest {

}

