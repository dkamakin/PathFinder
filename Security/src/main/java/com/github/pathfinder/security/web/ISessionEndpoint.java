package com.github.pathfinder.security.web;

import com.github.pathfinder.core.web.annotation.SwaggerDescriptionNonVoidMethod;
import com.github.pathfinder.security.web.dto.AuthenticationRequestDto;
import com.github.pathfinder.security.web.dto.AuthenticationResponseDto;
import com.github.pathfinder.security.web.dto.SessionRefreshRequestDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/user/session")
public interface ISessionEndpoint {

    @PostMapping
    @SwaggerDescriptionNonVoidMethod(
            description = "User authenticating",
            content = {@Content(schema = @Schema(implementation = AuthenticationResponseDto.class))}
    )
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);

    @PutMapping
    @SwaggerDescriptionNonVoidMethod(
            description = "User session refresh",
            content = {@Content(schema = @Schema(implementation = AuthenticationResponseDto.class))}
    )
    AuthenticationResponseDto refresh(SessionRefreshRequestDto request);

}
