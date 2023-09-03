package com.github.pathfinder.web;

import com.github.pathfinder.core.exception.ErrorMessage;
import com.github.pathfinder.web.dto.path.FindPathDto;
import com.github.pathfinder.web.dto.path.FoundPathDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface IPathEndpoint {

    @Operation(description = "Find terrain path", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = FoundPathDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
    })
    FoundPathDto find(FindPathDto request);

}
