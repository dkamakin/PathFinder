package com.github.pathfinder.security.database.repository;

import com.github.pathfinder.security.database.entity.UserRefreshTokenEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<UserRefreshTokenEntity, Long> {

    @Query("""
            SELECT token
            FROM UserRefreshTokenEntity token
            JOIN FETCH token.user
            WHERE token.user.id = :userId AND token.deviceName = :deviceName
            """)
    Optional<UserRefreshTokenEntity> find(@Param("userId") Long userId,
                                          @Param("deviceName") String deviceName);

}
