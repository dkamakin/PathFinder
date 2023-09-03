package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.exception.UserAlreadyRegisteredException;
import com.github.pathfinder.security.data.Mapper;
import com.github.pathfinder.security.data.user.User;
import com.github.pathfinder.security.database.entity.UserEntity;
import com.github.pathfinder.security.database.entity.UserRolesEntity;
import com.github.pathfinder.security.database.repository.UserRepository;
import com.github.pathfinder.security.service.ITokenService;
import com.github.pathfinder.security.service.IUserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ITokenService  tokenService;

    @Override
    @Transactional
    @Logged(ignoreReturnValue = false)
    public User save(User user) {
        userRepository.find(user.username()).ifPresent(registered -> {
            throw new UserAlreadyRegisteredException(registered.getName());
        });

        var rolesEntity = new UserRolesEntity(user.roles().role());
        var userEntity  = new UserEntity(user.username(), user.password(), rolesEntity);

        rolesEntity.setUser(userEntity);

        return Mapper.map(userRepository.save(userEntity));
    }

    @Override
    @Logged("username")
    @ReadTransactional
    public Optional<User> read(String username) {
        return userRepository
                .find(username)
                .map(Mapper::map);
    }

    @Override
    @ReadTransactional
    @Logged("username")
    public Optional<User> read(Token token) {
        return userRepository
                .find(tokenService.userInfo(token).username())
                .map(Mapper::map);
    }

}
