package com.github.pathfinder.security.configuration;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.core.tools.impl.JsonTools;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

@WebMvcTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(classes = {CoreConfiguration.class, JsonTools.class})
public @interface SecurityWebMvcTest {

}
