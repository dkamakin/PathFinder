package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    @Logged(ignoreReturnValue = false)
    public boolean matches(String rawPassword, String userPassword) {
        return passwordEncoder.matches(rawPassword, userPassword);
    }

    @Logged
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
