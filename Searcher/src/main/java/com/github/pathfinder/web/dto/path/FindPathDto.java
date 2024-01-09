package com.github.pathfinder.web.dto.path;

import com.github.pathfinder.web.dto.CoordinateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record FindPathDto(
        @Valid
        @NotNull
        @Schema(description = "Source point coordinates", requiredMode = Schema.RequiredMode.REQUIRED)
        CoordinateDto source,
        @Valid
        @NotNull
        @Schema(description = "Target point coordinates", requiredMode = Schema.RequiredMode.REQUIRED)
        CoordinateDto target) {

}
