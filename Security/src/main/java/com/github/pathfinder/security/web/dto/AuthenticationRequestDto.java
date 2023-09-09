package com.github.pathfinder.security.web.dto;

import com.github.pathfinder.security.data.user.UserConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthenticationRequestDto(
        @NotBlank
        @Size(min = UserConstant.NAME_MIN_LENGTH, max = UserConstant.NAME_MAX_LENGTH)
        @Schema(description = "Username", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin",
                maxLength = UserConstant.NAME_MAX_LENGTH,
                minLength = UserConstant.NAME_MIN_LENGTH)
        String username,
        @NotBlank
        @Size(min = UserConstant.PASSWORD_MIN_LENGTH, max = UserConstant.PASSWORD_MAX_LENGTH)
        @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin",
                minLength = UserConstant.PASSWORD_MIN_LENGTH,
                maxLength = UserConstant.PASSWORD_MAX_LENGTH)
        String password,
        @Valid
        @NotNull
        @Schema(description = "Device info", requiredMode = Schema.RequiredMode.REQUIRED)
        DeviceInfoDto device) {

}
