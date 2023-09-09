package com.github.pathfinder.security.web.dto;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SessionRefreshRequestDto(
        @NotBlank
        @Schema(description = "User refresh token (long-lived)", requiredMode = Schema.RequiredMode.REQUIRED,
                example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyNjQ0NjQwNiwiaWF0IjoxN")
        String refreshToken) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
