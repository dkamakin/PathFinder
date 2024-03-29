package com.github.pathfinder.indexer.configuration;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class IndexerDatabaseTestExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        ApplicationContext springContext = SpringExtension.getApplicationContext(context);
        springContext.getBean(IndexerTestDatabaseTemplate.class).cleanDatabase();
    }

}
