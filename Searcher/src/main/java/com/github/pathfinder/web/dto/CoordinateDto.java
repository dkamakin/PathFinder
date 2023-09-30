package com.github.pathfinder.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CoordinateDto(
        @NotNull
        @Schema(description = "Point longitude", requiredMode = Schema.RequiredMode.REQUIRED, example = "40.231")
        Double longitude,
        @NotNull
        @Schema(description = "Point latitude", requiredMode = Schema.RequiredMode.REQUIRED, example = "20.03")
        Double latitude) {

}