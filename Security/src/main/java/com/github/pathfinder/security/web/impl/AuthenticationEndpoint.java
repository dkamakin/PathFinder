package com.github.pathfinder.security.web.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.service.IAuthenticationService;
import com.github.pathfinder.security.web.IAuthenticationEndpoint;
import com.github.pathfinder.security.web.dto.AuthenticationRequestDto;
import com.github.pathfinder.security.web.dto.AuthenticationResponseDto;
import com.github.pathfinder.security.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/authenticate")
public class AuthenticationEndpoint implements IAuthenticationEndpoint {

    private final IAuthenticationService authenticationService;

    @Logged
    @Override
    @PostMapping
    public AuthenticationResponseDto authenticate(@RequestBody @Valid AuthenticationRequestDto request) {
        return WebMapper.map(authenticationService.authenticate(WebMapper.map(request)));
    }

}
