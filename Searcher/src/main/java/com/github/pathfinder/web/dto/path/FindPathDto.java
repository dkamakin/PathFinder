package com.github.pathfinder.web.dto.path;

import com.github.pathfinder.core.tools.impl.NullHelper;
import com.github.pathfinder.web.dto.CoordinateDto;
import com.github.pathfinder.web.dto.HealthTypeDto;
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
        CoordinateDto target,
        @NotNull
        @Schema(description = "User health status", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "HEALTHY")
        HealthTypeDto health) {

    public FindPathDto(CoordinateDto source, CoordinateDto target, HealthTypeDto health) {
        this.source = source;
        this.target = target;
        this.health = NullHelper.notNull(health, HealthTypeDto.HEALTHY);
    }

}
