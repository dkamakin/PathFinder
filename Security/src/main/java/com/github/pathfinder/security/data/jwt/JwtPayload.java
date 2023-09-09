package com.github.pathfinder.security.data.jwt;

import com.github.pathfinder.security.api.data.DeviceInfo;
import lombok.Builder;

@Builder
public record JwtPayload(Long userId,
                         DeviceInfo device) {

}
