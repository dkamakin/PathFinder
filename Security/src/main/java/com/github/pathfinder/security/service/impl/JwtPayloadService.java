package com.github.pathfinder.security.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.data.jwt.JwtConstants;
import com.github.pathfinder.security.data.jwt.JwtPayload;
import com.github.pathfinder.security.data.user.UserTokenInfo;
import com.github.pathfinder.security.service.IJwtPayloadService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtPayloadService implements IJwtPayloadService {

    @Logged
    @Override
    public Map<String, Object> toPayload(UserTokenInfo user) {
        return Map.of(JwtConstants.USERNAME, user.username());
    }

    @Logged
    @Override
    public JwtPayload fromDecoded(DecodedJWT decodedJWT) {
        var extractor = new JwtClaimExtractor(decodedJWT);

        return JwtPayload
                .builder()
                .username(extractor.username())
                .build();
    }

}
