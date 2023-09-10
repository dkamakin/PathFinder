package com.github.pathfinder.security.web;

import com.github.pathfinder.core.exception.ErrorMessage;
import com.github.pathfinder.security.web.dto.AuthenticationRequestDto;
import com.github.pathfinder.security.web.dto.AuthenticationResponseDto;
import com.github.pathfinder.security.web.dto.SessionRefreshRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/user/session")
public interface ISessionEndpoint {

    @PostMapping
    @Operation(description = "User authenticating", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = AuthenticationResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
    })
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);

    @PutMapping
    @Operation(description = "User session refresh", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = AuthenticationResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorMessage.class))}),
    })
    AuthenticationResponseDto refresh(SessionRefreshRequestDto request);

}
