package com.github.pathfinder.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CoordinateDto(
        @NotNull
        @Schema(description = "Point longitude", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
        Integer longitude,
        @NotNull
        @Schema(description = "Point latitude", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        Integer latitude) {

}