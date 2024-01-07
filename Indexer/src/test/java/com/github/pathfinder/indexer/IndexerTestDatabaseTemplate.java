package com.github.pathfinder.indexer;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.support.TransactionTemplate;

@TestComponent
@RequiredArgsConstructor
public class IndexerTestDatabaseTemplate {

    private final TransactionTemplate transactionTemplate;
    private final JdbcTemplate        jdbcTemplate;

    public void cleanDatabase() {
        transactionTemplate.execute(status -> {
            JdbcTestUtils.deleteFromTables(jdbcTemplate, IndexBoxEntity.Token.TABLE);
            return null;
        });
    }

}
