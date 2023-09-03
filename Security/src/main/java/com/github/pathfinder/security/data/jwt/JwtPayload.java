package com.github.pathfinder.security.data.jwt;

import lombok.Builder;

@Builder
public record JwtPayload(String username) {

}
