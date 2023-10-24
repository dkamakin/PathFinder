package com.github.pathfinder.security.configuration;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class SecurityDatabaseTestExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        ApplicationContext springContext = SpringExtension.getApplicationContext(context);
        springContext.getBean(SecurityTestDatabaseTemplate.class).cleanDatabase();
    }

}
