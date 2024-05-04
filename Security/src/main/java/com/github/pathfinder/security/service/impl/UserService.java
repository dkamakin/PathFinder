package com.github.pathfinder.security.service.impl;

import java.util.Optional;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.exception.UserAlreadyRegisteredException;
import com.github.pathfinder.security.data.user.SaveUserRequest;
import com.github.pathfinder.security.database.entity.UserEntity;
import com.github.pathfinder.security.database.entity.UserRolesEntity;
import com.github.pathfinder.security.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository  userRepository;
    private final TokenService    tokenService;
    private final PasswordService passwordService;

    @Transactional
    @Logged(ignoreReturnValue = false, value = "user")
    public UserEntity save(SaveUserRequest user) {
        userRepository.find(user.username()).ifPresent(registered -> {
            throw new UserAlreadyRegisteredException(registered.getName());
        });

        var password    = passwordService.encode(user.password());
        var rolesEntity = new UserRolesEntity(user.role());
        var userEntity  = new UserEntity(user.username(), password, rolesEntity);

        rolesEntity.setUser(userEntity);

        return userRepository.save(userEntity);
    }

    @ReadTransactional
    @Logged("username")
    public Optional<UserEntity> read(String username) {
        return userRepository.find(username);
    }

    @Logged
    @ReadTransactional
    public Optional<UserEntity> read(Token token) {
        return userRepository.findById(tokenService.payload(token).userId());
    }

}
