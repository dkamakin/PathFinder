package com.github.pathfinder.security.web.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.service.impl.AuthenticationService;
import com.github.pathfinder.security.web.ISessionEndpoint;
import com.github.pathfinder.security.web.dto.AuthenticationRequestDto;
import com.github.pathfinder.security.web.dto.AuthenticationResponseDto;
import com.github.pathfinder.security.web.dto.SessionRefreshRequestDto;
import com.github.pathfinder.security.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionEndpoint implements ISessionEndpoint {

    private final AuthenticationService authenticationService;

    @Logged
    @Override
    public AuthenticationResponseDto authenticate(@RequestBody @Valid AuthenticationRequestDto request) {
        var mapper = DtoMapper.INSTANCE;

        return mapper.authenticationResponse(authenticationService.authenticate(mapper.authenticationRequest(request)));
    }

    @Logged
    @Override
    public AuthenticationResponseDto refresh(@RequestBody @Valid SessionRefreshRequestDto request) {
        var mapper = DtoMapper.INSTANCE;

        return mapper.authenticationResponse(authenticationService.refresh(mapper.sessionRefreshRequest(request)));
    }

}
