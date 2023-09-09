package com.github.pathfinder.security.web.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.service.IAuthenticationService;
import com.github.pathfinder.security.web.ISessionEndpoint;
import com.github.pathfinder.security.web.dto.AuthenticationRequestDto;
import com.github.pathfinder.security.web.dto.AuthenticationResponseDto;
import com.github.pathfinder.security.web.dto.SessionRefreshRequestDto;
import com.github.pathfinder.security.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user/session")
public class SessionEndpoint implements ISessionEndpoint {

    private final IAuthenticationService authenticationService;

    @Logged
    @Override
    @PostMapping
    public AuthenticationResponseDto authenticate(@RequestBody @Valid AuthenticationRequestDto request) {
        var mapper = DtoMapper.INSTANCE;

        return mapper.authenticationResponse(authenticationService.authenticate(mapper.authenticationRequest(request)));
    }

    @Logged
    @Override
    @PutMapping
    public AuthenticationResponseDto refresh(@RequestBody @Valid SessionRefreshRequestDto request) {
        var mapper = DtoMapper.INSTANCE;

        return mapper.authenticationResponse(authenticationService.refresh(mapper.sessionRefreshRequest(request)));
    }

}
