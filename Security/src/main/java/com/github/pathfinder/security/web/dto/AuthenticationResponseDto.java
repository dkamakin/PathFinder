package com.github.pathfinder.security.web.dto;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationResponseDto(
        @Schema(description = "User session token")
        String token) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("token", "***")
                .toString();
    }
}
