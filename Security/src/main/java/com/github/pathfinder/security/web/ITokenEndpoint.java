package com.github.pathfinder.security.web;

import com.github.pathfinder.core.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface ITokenEndpoint {

    @Operation(description = "Check token availability", responses = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
    })
    void isValid();

}
