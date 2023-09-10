package com.github.pathfinder.security.web.dto;

import com.github.pathfinder.security.data.user.UserConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeviceInfoDto(
        @NotBlank
        @Size(max = UserConstant.DEVICE_NAME_MAX_LENGTH)
        @Schema(description = "Device name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Windows 10 my PC")
        String name) {

}
