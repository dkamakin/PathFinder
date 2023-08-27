package com.github.pathfinder.security;

import com.github.pathfinder.security.data.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;

public class UserFixtures {

    public static final String      USERNAME     = "username";
    public static final String      PASSWORD     = "password";
    public static final UserDetails USER_DETAILS = new UserDetailsImpl(USERNAME, PASSWORD);

}
