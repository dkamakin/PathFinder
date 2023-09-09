package com.github.pathfinder.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pathfinder.security.data.jwt.JwtPayload;
import java.util.Map;

public interface IJwtPayloadService {

    Map<String, Object> toPayload(JwtPayload payload);

    JwtPayload fromDecoded(DecodedJWT decodedJWT);

}
