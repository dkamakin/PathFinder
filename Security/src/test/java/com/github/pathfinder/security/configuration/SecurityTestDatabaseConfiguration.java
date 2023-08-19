package com.github.pathfinder.security.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({SecurityTestDatabaseTemplate.class})
public class SecurityTestDatabaseConfiguration {

}
