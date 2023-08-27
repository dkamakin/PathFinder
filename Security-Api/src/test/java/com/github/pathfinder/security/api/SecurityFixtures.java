package com.github.pathfinder.security.api;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class SecurityFixtures {

    public static final String         PASSWORD       = "password";
    public static final String         USERNAME       = "username";
    public static final Authentication AUTHENTICATION = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);

}
