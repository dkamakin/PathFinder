package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.security.data.Mapper;
import com.github.pathfinder.security.database.entity.UserEntity;
import com.github.pathfinder.security.database.repository.UserRepository;
import com.github.pathfinder.security.exception.UserAlreadyRegisteredException;
import com.github.pathfinder.security.exception.UserNotFoundException;
import com.github.pathfinder.security.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor // TODO handle roles
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    @Logged(logException = true)
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return userRepository
                .find(user.getUsername())
                .map(userEntity -> userEntity.setPassword(newPassword))
                .map(Mapper::map)
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));
    }

    @Override
    @Transactional
    @Logged(logException = true)
    public void createUser(UserDetails user) {
        userRepository
                .find(user.getUsername())
                .ifPresentOrElse(userEntity -> {
                                     throw new UserAlreadyRegisteredException(user);
                                 },
                                 () -> userRepository.save(new UserEntity(user)));
    }

    @Override
    @Transactional
    @Logged(logException = true)
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    @Logged(logException = true)
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    @Logged(logException = true)
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @ReadTransactional
    @Logged(logException = true)
    public boolean userExists(String username) {
        return userRepository.find(username).isPresent();
    }

    @Override
    @ReadTransactional
    @Logged(logException = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .find(username)
                .map(Mapper::map)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

}
