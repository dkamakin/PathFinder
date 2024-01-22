package com.github.pathfinder.web.dto.path;

import com.github.pathfinder.web.dto.CoordinateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record FoundPathDto(
        @Schema(description = "List of the path coordinates")
        List<CoordinateDto> path,
        @Schema(description = "Path distance in meters", example = "1324.2")
        Double meters) {

}
