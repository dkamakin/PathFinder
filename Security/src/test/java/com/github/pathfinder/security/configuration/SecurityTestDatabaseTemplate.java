package com.github.pathfinder.security.configuration;

import com.github.pathfinder.security.database.entity.UserEntity;
import com.github.pathfinder.security.database.entity.UserRefreshTokenEntity;
import com.github.pathfinder.security.database.entity.UserRolesEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@RequiredArgsConstructor
public class SecurityTestDatabaseTemplate {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void cleanDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,
                                       UserRefreshTokenEntity.Token.TABLE,
                                       UserRolesEntity.Token.TABLE,
                                       UserEntity.Token.TABLE);
    }

}
