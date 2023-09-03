package com.github.pathfinder.security.service;

import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.data.user.UserTokenInfo;

public interface ITokenService {

    Token issue(UserTokenInfo user);

    UserTokenInfo userInfo(Token token);

}
