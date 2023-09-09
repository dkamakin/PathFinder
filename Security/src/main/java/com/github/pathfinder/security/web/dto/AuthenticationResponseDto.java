package com.github.pathfinder.security.web.dto;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationResponseDto(
        @Schema(description = "User access token (short-lived)", example = "eyJhbGci0iJIUzI1NiIsInR5cCI6IkpXVCJ9")
        String accessToken,
        @Schema(description = "User refresh token (long-lived)", example = "eyJhbGci0iJIUzI1NiIsInR5cCI6IkpXVCJ9")
        String refreshToken) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
