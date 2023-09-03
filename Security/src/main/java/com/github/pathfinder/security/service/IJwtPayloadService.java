package com.github.pathfinder.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pathfinder.security.data.jwt.JwtPayload;
import com.github.pathfinder.security.data.user.UserTokenInfo;
import java.util.Map;

public interface IJwtPayloadService {

    Map<String, Object> toPayload(UserTokenInfo user);

    JwtPayload fromDecoded(DecodedJWT decodedJWT);

}
