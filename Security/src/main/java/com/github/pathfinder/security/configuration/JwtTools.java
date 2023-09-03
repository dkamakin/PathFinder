package com.github.pathfinder.security.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Getter
@Component
@RefreshScope
public class JwtTools {

    private final TokenConfiguration configuration;
    private final Algorithm          algorithm;
    private final JWTVerifier        verifier;

    public JwtTools(TokenConfiguration configuration) {
        this.configuration = configuration;
        this.algorithm     = Algorithm.HMAC512(configuration.getSecret());
        this.verifier      = JWT.require(algorithm).build();
    }

    public JWTCreator.Builder builder() {
        return JWT.create();
    }

}
