package com.github.pathfinder.indexer.configuration;

import com.github.pathfinder.database.test.DatabaseTestTemplate;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class IndexerDatabaseTestExtension implements BeforeEachCallback, BeforeAllCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        ApplicationContext springContext = SpringExtension.getApplicationContext(context);
        springContext.getBean(IndexerTestDatabaseTemplate.class).cleanDatabase();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        new DatabaseTestTemplate().initialize();
    }

}
