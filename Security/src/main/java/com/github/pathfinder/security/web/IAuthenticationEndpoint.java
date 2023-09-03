package com.github.pathfinder.security.web;

import com.github.pathfinder.core.exception.ErrorMessage;
import com.github.pathfinder.security.web.dto.AuthenticationRequestDto;
import com.github.pathfinder.security.web.dto.AuthenticationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface IAuthenticationEndpoint {

    @Operation(description = "User authenticating", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = AuthenticationResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
    })
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);

}
