package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.service.IPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService implements IPasswordService {

    private final PasswordEncoder passwordEncoder;

    @Override
    @Logged(ignoreReturnValue = false)
    public boolean matches(String rawPassword, String userPassword) {
        return passwordEncoder.matches(rawPassword, userPassword);
    }
}
