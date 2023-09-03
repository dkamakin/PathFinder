package com.github.pathfinder.security.web.mapper;

import com.github.pathfinder.security.api.data.AuthenticationRequest;
import com.github.pathfinder.security.api.data.AuthenticationResponse;
import com.github.pathfinder.security.web.dto.AuthenticationRequestDto;
import com.github.pathfinder.security.web.dto.AuthenticationResponseDto;

public class WebMapper {

    public static AuthenticationResponseDto map(AuthenticationResponse response) {
        return new AuthenticationResponseDto(response.token().value());
    }

    public static AuthenticationRequest map(AuthenticationRequestDto request) {
        return new AuthenticationRequest(request.username(), request.password());
    }

}
