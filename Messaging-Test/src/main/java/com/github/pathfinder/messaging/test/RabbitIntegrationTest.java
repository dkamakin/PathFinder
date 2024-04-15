package com.github.pathfinder.messaging.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.messaging.configuration.MessagingConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Target(ElementType.TYPE)
@ExtendWith(RabbitExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {
        LocalValidatorFactoryBean.class,
        CoreConfiguration.class,
        MessagingConfiguration.class
})
public @interface RabbitIntegrationTest {

}

