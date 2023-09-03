package com.github.pathfinder.security.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pathfinder.security.api.exception.InvalidTokenException;
import com.github.pathfinder.security.data.jwt.JwtConstants;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtClaimExtractor {

    private final DecodedJWT decodedJWT;

    public String username() {
        return claim(JwtConstants.USERNAME, Claim::asString);
    }

    private <T> T claim(String name, Function<Claim, T> mapper) {
        var extracted = mapper.apply(decodedJWT.getClaim(name));

        if (extracted == null) {
            throw new InvalidTokenException();
        }

        return extracted;
    }

}
