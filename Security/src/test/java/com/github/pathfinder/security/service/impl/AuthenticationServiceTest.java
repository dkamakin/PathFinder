package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.security.SecurityFixtures;
import com.github.pathfinder.security.api.data.AuthenticationRequest;
import com.github.pathfinder.security.api.data.SessionRefreshRequest;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.data.Tokens;
import com.github.pathfinder.security.api.exception.InvalidCredentialsException;
import com.github.pathfinder.security.api.exception.InvalidTokenException;
import com.github.pathfinder.security.api.exception.UserNotFoundException;
import com.github.pathfinder.security.configuration.SecurityServiceDatabaseTest;
import com.github.pathfinder.security.configuration.SecurityTestDatabaseTemplate;
import com.github.pathfinder.security.data.jwt.JwtPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SecurityServiceDatabaseTest
@Import({AuthenticationService.class, UserService.class, PasswordService.class, BCryptPasswordEncoder.class})
class AuthenticationServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    TokenService tokenService;

    @Autowired
    PasswordService passwordService;

    @Autowired
    AuthenticationService target;

    @Autowired
    SecurityTestDatabaseTemplate securityTestDatabaseTemplate;

    @BeforeEach
    void setUp() {
        securityTestDatabaseTemplate.cleanDatabase();
    }

    void whenNeedToIssueTokens(JwtPayload payload, Tokens first, Tokens... expected) {
        when(tokenService.issue(payload)).thenReturn(first, expected);
    }

    void whenNeedToReadToken(Token token, JwtPayload expected) {
        when(tokenService.payload(token)).thenReturn(expected);
    }

    void whenTokenIsNotValid(Token token) {
        when(tokenService.payload(token)).thenThrow(InvalidTokenException.class);
    }

    @Test
    void authenticate_UserNotFound_UserNotFoundException() {
        var request = new AuthenticationRequest(SecurityFixtures.USERNAME, SecurityFixtures.PASSWORD, null);

        assertThatThrownBy(() -> target.authenticate(request)).isInstanceOf(UserNotFoundException.class);

        verifyNoInteractions(tokenService);
    }

    @Test
    void authenticate_PasswordDoesNotMatch_InvalidCredentialsException() {
        var user    = userService.save(SecurityFixtures.SAVE_USER_REQUEST);
        var request = new AuthenticationRequest(user.getName(), "invalidPassword", null);

        assertThatThrownBy(() -> target.authenticate(request)).isInstanceOf(InvalidCredentialsException.class);

        verifyNoInteractions(tokenService);
    }

    @Test
    void authenticate_UserFoundAndPasswordMatch_IssueAndPersistToken() {
        var saveRequest = SecurityFixtures.SAVE_USER_REQUEST;
        var user        = userService.save(saveRequest);
        var payload     = new JwtPayload(user.getId(), SecurityFixtures.DEVICE_INFO);
        var request     = new AuthenticationRequest(user.getName(), saveRequest.password(), payload.device());
        var tokens      = SecurityFixtures.tokens();

        whenNeedToIssueTokens(payload, tokens);

        var actual = target.authenticate(request);

        assertThat(actual)
                .matches(result -> result.accessToken().equals(tokens.accessToken()))
                .matches(result -> result.refreshToken().equals(tokens.refreshToken()));
    }

    @Test
    void authenticate_UserFoundAndPasswordMatch_DeleteOldTokens() {
        var saveRequest = SecurityFixtures.SAVE_USER_REQUEST;
        var user        = userService.save(saveRequest);
        var payload     = new JwtPayload(user.getId(), SecurityFixtures.DEVICE_INFO);
        var request     = new AuthenticationRequest(user.getName(), saveRequest.password(), payload.device());

        whenNeedToIssueTokens(payload, SecurityFixtures.tokens(), SecurityFixtures.tokens());

        var firstAuthentication  = target.authenticate(request);
        var secondAuthentication = target.authenticate(request);
        var refreshRequest       = new SessionRefreshRequest(firstAuthentication.refreshToken());

        whenNeedToReadToken(refreshRequest.refreshToken(), payload);

        assertThatThrownBy(() -> target.refresh(refreshRequest)).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void refresh_TokenNotFound_InvalidTokenException() {
        var payload = new JwtPayload(-1L, SecurityFixtures.DEVICE_INFO);
        var request = new SessionRefreshRequest(SecurityFixtures.token());

        whenNeedToReadToken(request.refreshToken(), payload);

        assertThatThrownBy(() -> target.refresh(request)).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void refresh_TokenIsValid_DeletePreviousTokensAndIssueNewOnes() {
        var saveRequest           = SecurityFixtures.SAVE_USER_REQUEST;
        var user                  = userService.save(saveRequest);
        var device                = SecurityFixtures.DEVICE_INFO;
        var payload               = new JwtPayload(user.getId(), device);
        var authenticationRequest = new AuthenticationRequest(user.getName(), saveRequest.password(), device);

        whenNeedToIssueTokens(payload, SecurityFixtures.tokens(), SecurityFixtures.tokens());

        var authenticated = target.authenticate(authenticationRequest);
        var request       = new SessionRefreshRequest(authenticated.refreshToken());

        whenNeedToReadToken(request.refreshToken(), payload);

        var refreshed           = target.refresh(request);
        var requestAfterRefresh = new SessionRefreshRequest(refreshed.refreshToken());

        whenNeedToReadToken(requestAfterRefresh.refreshToken(), payload);

        assertThatThrownBy(() -> target.refresh(request)).isInstanceOf(InvalidTokenException.class);
        assertThatThrownBy(() -> target.refresh(requestAfterRefresh)).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void refresh_TokenIsInvalidOrExpired_InvalidTokenException() {
        var saveRequest           = SecurityFixtures.SAVE_USER_REQUEST;
        var user                  = userService.save(saveRequest);
        var device                = SecurityFixtures.DEVICE_INFO;
        var payload               = new JwtPayload(user.getId(), device);
        var authenticationRequest = new AuthenticationRequest(user.getName(), saveRequest.password(), device);

        whenNeedToIssueTokens(payload, SecurityFixtures.tokens(), SecurityFixtures.tokens());

        var authenticated = target.authenticate(authenticationRequest);
        var request       = new SessionRefreshRequest(authenticated.refreshToken());

        whenTokenIsNotValid(request.refreshToken());

        assertThatThrownBy(() -> target.refresh(request)).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void refresh_TokenIsValid_RefreshUpdatesAuthenticationToken() {
        var saveRequest           = SecurityFixtures.SAVE_USER_REQUEST;
        var user                  = userService.save(saveRequest);
        var device                = SecurityFixtures.DEVICE_INFO;
        var authenticationRequest = new AuthenticationRequest(user.getName(), saveRequest.password(), device);
        var payload               = new JwtPayload(user.getId(), device);

        whenNeedToIssueTokens(payload, SecurityFixtures.tokens(), SecurityFixtures.tokens(), SecurityFixtures.tokens());

        var authenticated = target.authenticate(authenticationRequest);
        var request       = new SessionRefreshRequest(authenticated.refreshToken());

        whenNeedToReadToken(request.refreshToken(), payload);

        target.refresh(request);

        assertThatThrownBy(() -> target.refresh(request)).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void refresh_TokenIsValid_RefreshUpdatesTokens() {
        var saveRequest           = SecurityFixtures.SAVE_USER_REQUEST;
        var user                  = userService.save(saveRequest);
        var device                = SecurityFixtures.DEVICE_INFO;
        var authenticationRequest = new AuthenticationRequest(user.getName(), saveRequest.password(), device);
        var payload               = new JwtPayload(user.getId(), device);

        whenNeedToIssueTokens(payload, SecurityFixtures.tokens(), SecurityFixtures.tokens(), SecurityFixtures.tokens());

        var authenticated = target.authenticate(authenticationRequest);
        var request       = new SessionRefreshRequest(authenticated.refreshToken());

        whenNeedToReadToken(request.refreshToken(), payload);

        var refreshed           = target.refresh(request);
        var requestAfterRefresh = new SessionRefreshRequest(refreshed.refreshToken());

        whenNeedToReadToken(requestAfterRefresh.refreshToken(), payload);

        var secondRefresh = target.refresh(requestAfterRefresh);

        assertThat(secondRefresh)
                .matches(actual -> !actual.refreshToken().equals(refreshed.refreshToken()))
                .matches(actual -> !actual.accessToken().equals(refreshed.accessToken()));
    }

    @Test
    void refresh_TokenIsValid_RefreshWithAccessTokenNotPossible() {
        var saveRequest           = SecurityFixtures.SAVE_USER_REQUEST;
        var user                  = userService.save(saveRequest);
        var device                = SecurityFixtures.DEVICE_INFO;
        var authenticationRequest = new AuthenticationRequest(user.getName(), saveRequest.password(), device);
        var payload               = new JwtPayload(user.getId(), device);

        whenNeedToIssueTokens(payload, SecurityFixtures.tokens(), SecurityFixtures.tokens());

        var authenticated = target.authenticate(authenticationRequest);
        var request       = new SessionRefreshRequest(authenticated.accessToken());

        whenNeedToReadToken(request.refreshToken(), payload);

        assertThatThrownBy(() -> target.refresh(request)).isInstanceOf(InvalidTokenException.class);
    }
}
