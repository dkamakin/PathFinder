package com.github.pathfinder.security.api;

import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.data.UserInfo;

public class SecurityFixtures {

    public static final Token    TOKEN     = new Token("accessToken");
    public static final UserInfo USER_INFO = new UserInfo("username", "accessToken");

}
