package com.github.pathfinder.security.service.impl;

import java.util.Map;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.api.data.DeviceInfo;
import com.github.pathfinder.security.data.jwt.JwtConstants;
import com.github.pathfinder.security.data.jwt.JwtPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtPayloadService {

    @Logged
    public Map<String, Object> toPayload(JwtPayload payload) {
        return Map.of(JwtConstants.USER_ID, payload.userId(),
                      JwtConstants.DEVICE_NAME, payload.device().name());
    }

    @Logged
    public JwtPayload fromDecoded(DecodedJWT decodedJWT) {
        var extractor = new JwtClaimExtractor(decodedJWT);

        return JwtPayload
                .builder()
                .userId(extractor.userId())
                .device(new DeviceInfo(extractor.deviceName()))
                .build();
    }

}
