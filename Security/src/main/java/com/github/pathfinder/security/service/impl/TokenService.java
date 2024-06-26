package com.github.pathfinder.security.service.impl;

import java.util.Map;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.security.api.data.SecurityApiMapper;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.data.Tokens;
import com.github.pathfinder.security.api.exception.InvalidTokenException;
import com.github.pathfinder.security.configuration.JwtTools;
import com.github.pathfinder.security.data.jwt.JwtPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class TokenService {

    private final JwtPayloadService payloadService;
    private final JwtTools          jwtTools;
    private final IDateTimeSupplier timeSupplier;

    @Logged(ignoreReturnValue = false)
    public Tokens issue(JwtPayload payload) {
        var now                  = timeSupplier.now();
        var builder              = jwtTools.builder().withPayload(payload(payload)).withIssuedAt(now);
        var accessTokenLifetime  = jwtTools.getConfiguration().getAccessTokenLifetime();
        var refreshTokenLifetime = jwtTools.getConfiguration().getRefreshTokenLifetime();
        var accessToken          = builder.withExpiresAt(now.plus(accessTokenLifetime)).sign(jwtTools.getAlgorithm());
        var refreshToken         = builder.withExpiresAt(now.plus(refreshTokenLifetime)).sign(jwtTools.getAlgorithm());

        log.info("Issued the tokens, accessToken lifetime: {}, refreshToken lifetime: {}", accessTokenLifetime,
                 refreshTokenLifetime);

        return SecurityApiMapper.INSTANCE.tokens(accessToken, refreshToken);
    }

    @Logged(ignoreReturnValue = false)
    public JwtPayload payload(Token token) {
        return payloadService.fromDecoded(decode(token.value()));
    }

    private DecodedJWT decode(String token) {
        try {
            return jwtTools.getVerifier().verify(token);
        } catch (Exception e) {
            log.info("Token is invalid: {}", e.getMessage());
            throw new InvalidTokenException();
        }
    }

    private Map<String, Object> payload(JwtPayload payload) {
        return payloadService.toPayload(payload);
    }

}
