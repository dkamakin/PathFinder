package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.service.ISecurityService;
import com.github.pathfinder.security.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService implements ISecurityService {

    private final PasswordEncoder passwordEncoder;
    private final IUserService    userService;

    @Override
    @Logged(arguments = {"username"})
    public boolean isValid(String username, String password) {
        return passwordEncoder.matches(password, userService.loadUserByUsername(username).getPassword());
    }

}
