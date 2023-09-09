package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.exception.UserAlreadyRegisteredException;
import com.github.pathfinder.security.data.user.SaveUserRequest;
import com.github.pathfinder.security.database.entity.UserEntity;
import com.github.pathfinder.security.database.entity.UserRolesEntity;
import com.github.pathfinder.security.database.repository.UserRepository;
import com.github.pathfinder.security.service.IPasswordService;
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

    private final UserRepository   userRepository;
    private final ITokenService    tokenService;
    private final IPasswordService passwordService;

    @Override
    @Transactional
    @Logged(ignoreReturnValue = false)
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

    @Override
    @Logged("username")
    @ReadTransactional
    public Optional<UserEntity> read(String username) {
        return userRepository.find(username);
    }

    @Override
    @ReadTransactional
    @Logged("username")
    public Optional<UserEntity> read(Token token) {
        return userRepository.findById(tokenService.payload(token).userId());
    }

}
