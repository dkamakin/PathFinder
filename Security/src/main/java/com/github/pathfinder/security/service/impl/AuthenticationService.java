package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.api.data.AuthenticationRequest;
import com.github.pathfinder.security.api.data.AuthenticationResponse;
import com.github.pathfinder.security.api.data.SecurityApiMapper;
import com.github.pathfinder.security.api.data.SessionRefreshRequest;
import com.github.pathfinder.security.api.data.Tokens;
import com.github.pathfinder.security.api.exception.InvalidCredentialsException;
import com.github.pathfinder.security.api.exception.InvalidTokenException;
import com.github.pathfinder.security.api.exception.UserNotFoundException;
import com.github.pathfinder.security.data.jwt.JwtPayload;
import com.github.pathfinder.security.database.entity.UserEntity;
import com.github.pathfinder.security.database.entity.UserRefreshTokenEntity;
import com.github.pathfinder.security.database.repository.RefreshTokenRepository;
import com.github.pathfinder.security.service.IAuthenticationService;
import com.github.pathfinder.security.service.IPasswordService;
import com.github.pathfinder.security.service.ITokenService;
import com.github.pathfinder.security.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final ITokenService          tokenService;
    private final IPasswordService       passwordService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final IUserService           userService;

    @Override
    @Transactional
    @Logged("request")
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return userService
                .read(request.username())
                .map(user -> authenticate(request, user))
                .map(SecurityApiMapper.INSTANCE::authenticationResponse)
                .orElseThrow(() -> new UserNotFoundException(request.username()));
    }

    @Override
    @Logged("request")
    @Transactional(noRollbackFor = InvalidTokenException.class)
    public AuthenticationResponse refresh(SessionRefreshRequest request) {
        return refresh(tokenService.payload(request.refreshToken()), request.refreshToken().value());
    }

    private AuthenticationResponse refresh(JwtPayload payload, String token) {
        return refreshTokenRepository
                .find(payload.userId(), payload.device().name())
                .map(actual -> refresh(actual, token, payload))
                .map(SecurityApiMapper.INSTANCE::authenticationResponse)
                .orElseThrow(InvalidTokenException::new);
    }

    private void checkRefreshToken(UserRefreshTokenEntity refreshToken, String request) {
        if (refreshToken.getToken().equals(request)) {
            log.info("Refresh token equals to the one in the database");
            return;
        }

        log.info("Found an attempt to use an invalid refresh token for user: {}", refreshToken.getUser().getId());

        refreshTokenRepository.delete(refreshToken);
        throw new InvalidTokenException();
    }

    private Tokens refresh(UserRefreshTokenEntity refreshToken, String request, JwtPayload payload) {
        checkRefreshToken(refreshToken, request);

        return issue(payload, refreshToken.getUser());
    }

    private Tokens authenticate(AuthenticationRequest request, UserEntity user) {
        checkCredentials(request, user);

        return issue(new JwtPayload(user.getId(), request.device()), user);
    }

    private Tokens issue(JwtPayload payload, UserEntity user) {
        log.info("Issuing new tokens and removing old ones for user: {}", user.getId());

        user.getRefreshTokens().clear();

        var tokens       = tokenService.issue(payload);
        var refreshToken = new UserRefreshTokenEntity(payload.device().name(), tokens.refreshToken().value(), user);

        refreshTokenRepository.save(refreshToken);

        return tokens;
    }

    private void checkCredentials(AuthenticationRequest request, UserEntity user) {
        if (!passwordService.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
    }

}
