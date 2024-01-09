package com.github.pathfinder.web.dto.path;

import com.github.pathfinder.web.dto.CoordinateDto;
import java.util.List;

public record FoundPathDto(List<CoordinateDto> path,
                           Double meters) {

}
