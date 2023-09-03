package com.github.pathfinder.security.database.repository;

import com.github.pathfinder.security.database.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query(value = """
            SELECT user
            FROM UserEntity user
            JOIN FETCH user.roles
            WHERE user.name = :username
            """)
    Optional<UserEntity> find(@Param("username") String username);

}
