package com.github.pathfinder.configuration;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.core.tools.JsonTools;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

@Inherited
@WebMvcTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(classes = {SearcherWebTestConfiguration.class, CoreConfiguration.class, JsonTools.class})
public @interface SearcherWebMvcTest {
}
