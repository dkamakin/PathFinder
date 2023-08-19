package com.github.pathfinder.security.configuration;

import com.github.pathfinder.security.database.entity.UserEntity;
import com.github.pathfinder.security.database.entity.UserRolesEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.support.TransactionTemplate;

@TestComponent
@RequiredArgsConstructor
public class SecurityTestDatabaseTemplate {

    private final TransactionTemplate transactionTemplate;
    private final JdbcTemplate        jdbcTemplate;

    public void cleanDatabase() {
        transactionTemplate.execute(status -> {
            JdbcTestUtils.deleteFromTables(jdbcTemplate, UserEntity.Token.TABLE, UserRolesEntity.Token.TABLE);
            return null;
        });
    }

}
