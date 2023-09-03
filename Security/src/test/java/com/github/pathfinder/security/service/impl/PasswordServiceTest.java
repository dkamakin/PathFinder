package com.github.pathfinder.security.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PasswordServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    PasswordService passwordService;

    @Test
    void matches_ValidRequest_CallPasswordEncoder() {
        var rawPassword  = "test";
        var userPassword = "hash";

        passwordService.matches(rawPassword, userPassword);

        verify(passwordEncoder).matches(rawPassword, userPassword);
    }

}
