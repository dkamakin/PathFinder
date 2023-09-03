package com.github.pathfinder.security.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.security.api.data.Mapper;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.exception.InvalidTokenException;
import com.github.pathfinder.security.configuration.JwtTools;
import com.github.pathfinder.security.data.user.UserTokenInfo;
import com.github.pathfinder.security.service.IJwtPayloadService;
import com.github.pathfinder.security.service.ITokenService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {

    private final IJwtPayloadService payloadService;
    private final JwtTools           jwtTools;
    private final IDateTimeSupplier  timeSupplier;

    @Logged
    @Override
    public Token issue(UserTokenInfo user) {
        return Mapper.token(token(user));
    }

    @Override
    @Logged(ignoreReturnValue = false)
    public UserTokenInfo userInfo(Token token) {
        var decoded = decode(token.value());
        var payload = payloadService.fromDecoded(decoded);

        return new UserTokenInfo(payload.username());
    }

    private DecodedJWT decode(String token) {
        try {
            return jwtTools.getVerifier().verify(token);
        } catch (Exception e) {
            log.info("Token is invalid: {}", e.getMessage());
            throw new InvalidTokenException();
        }
    }

    private String token(UserTokenInfo user) {
        var now      = timeSupplier.instant();
        var lifetime = jwtTools.getConfiguration().getLifeTime();
        var deadline = now.plus(lifetime);

        log.info("Issuing a token, lifetime: {}, expiring: {}", lifetime, deadline);

        return jwtTools
                .builder()
                .withPayload(payload(user))
                .withExpiresAt(deadline)
                .withIssuedAt(now)
                .sign(jwtTools.getAlgorithm());
    }

    private Map<String, Object> payload(UserTokenInfo user) {
        return payloadService.toPayload(user);
    }
}
