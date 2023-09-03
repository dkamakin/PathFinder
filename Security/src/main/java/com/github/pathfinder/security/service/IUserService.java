package com.github.pathfinder.security.service;

import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.data.user.User;
import java.util.Optional;

public interface IUserService {

    User save(User user);

    Optional<User> read(String username);

    Optional<User> read(Token token);

}
