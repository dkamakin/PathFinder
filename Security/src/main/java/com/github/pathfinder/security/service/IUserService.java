package com.github.pathfinder.security.service;

import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.data.user.SaveUserRequest;
import com.github.pathfinder.security.database.entity.UserEntity;
import java.util.Optional;

public interface IUserService {

    UserEntity save(SaveUserRequest user);

    Optional<UserEntity> read(String username);

    Optional<UserEntity> read(Token token);

}
