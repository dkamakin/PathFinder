package com.github.pathfinder.security;

import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.data.user.User;
import com.github.pathfinder.security.data.user.UserInternal;
import com.github.pathfinder.security.data.user.UserRoles;
import com.github.pathfinder.security.data.user.UserRolesInternal;

public class SecurityFixtures {

    public static final String    USERNAME   = "username";
    public static final String    PASSWORD   = "password";
    public static final String    ROLE       = "role";
    public static final Token     TOKEN      = new Token("token");
    public static final UserRoles USER_ROLES = new UserRolesInternal(ROLE);
    public static final User      USER       = new UserInternal(USERNAME, PASSWORD, USER_ROLES);

}
