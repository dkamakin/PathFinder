package com.github.pathfinder.security.web.dto;

import com.github.pathfinder.security.data.user.UserConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AuthenticationRequestDto(
        @Size(min = UserConstant.NAME_MIN_LENGTH, max = UserConstant.NAME_MAX_LENGTH)
        @Schema(description = "Username", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
        String username,
        @NotEmpty
        @Size(min = UserConstant.PASSWORD_MIN_LENGTH, max = UserConstant.PASSWORD_MAX_LENGTH)
        @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
        String password) {

}
