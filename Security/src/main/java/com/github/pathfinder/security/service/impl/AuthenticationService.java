package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.security.api.data.AuthenticationRequest;
import com.github.pathfinder.security.api.data.AuthenticationResponse;
import com.github.pathfinder.security.api.data.Mapper;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.exception.InvalidCredentialsException;
import com.github.pathfinder.security.api.exception.UserNotFoundException;
import com.github.pathfinder.security.data.user.User;
import com.github.pathfinder.security.data.user.UserTokenInfo;
import com.github.pathfinder.security.service.IAuthenticationService;
import com.github.pathfinder.security.service.IPasswordService;
import com.github.pathfinder.security.service.ITokenService;
import com.github.pathfinder.security.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final IUserService     userService;
    private final ITokenService    tokenService;
    private final IPasswordService passwordService;

    @Override
    @Logged("request")
    @ReadTransactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userService.read(request.username())
                .orElseThrow(() -> new UserNotFoundException(request.username()));

        if (passwordService.matches(request.password(), user.password())) {
            log.debug("Successfully authorized");
            return Mapper.authenticationResponse(token(user));
        } else {
            log.debug("Failed to authorize");
            throw new InvalidCredentialsException();
        }
    }

    private Token token(User user) {
        return tokenService.issue(new UserTokenInfo(user.username()));
    }

}
