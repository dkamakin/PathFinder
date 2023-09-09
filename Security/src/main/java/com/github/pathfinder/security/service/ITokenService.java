package com.github.pathfinder.security.service;

import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.data.Tokens;
import com.github.pathfinder.security.data.jwt.JwtPayload;

public interface ITokenService {

    Tokens issue(JwtPayload payload);

    JwtPayload payload(Token token);

}
