package com.github.pathfinder.indexer.configuration;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@RequiredArgsConstructor
public class IndexerTestDatabaseTemplate {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void cleanDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IndexBoxEntity.Token.TABLE);
    }

}
