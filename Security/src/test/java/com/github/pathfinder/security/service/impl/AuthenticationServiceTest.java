package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.security.SecurityFixtures;
import com.github.pathfinder.security.api.data.AuthenticationRequest;
import com.github.pathfinder.security.api.data.AuthenticationResponse;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.exception.InvalidCredentialsException;
import com.github.pathfinder.security.api.exception.UserNotFoundException;
import com.github.pathfinder.security.data.user.User;
import com.github.pathfinder.security.data.user.UserTokenInfo;
import com.github.pathfinder.security.service.IPasswordService;
import com.github.pathfinder.security.service.ITokenService;
import com.github.pathfinder.security.service.IUserService;
import java.util.Optional;
import javax.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    static class Data {
        static final AuthenticationRequest AUTHENTICATION_REQUEST = new AuthenticationRequest("wow",
                                                                                              "not so good password");
    }

    @Mock
    IUserService userService;

    @Mock
    ITokenService tokenService;

    @Mock
    IPasswordService passwordService;

    @InjectMocks
    AuthenticationService target;

    void whenNeedToGetUser(@Nullable User user, String expected) {
        doReturn(Optional.ofNullable(user)).when(userService).read(expected);
    }

    void whenNeedToCheckPasswordMatch(String providedPassword, String userPassword, boolean expected) {
        doReturn(expected).when(passwordService).matches(providedPassword, userPassword);
    }

    void whenNeedToIssueToken(UserTokenInfo user, Token expected) {
        doReturn(expected).when(tokenService).issue(user);
    }

    @Test
    void authenticate_UserNotFound_UserNotFoundException() {
        var request = Data.AUTHENTICATION_REQUEST;

        whenNeedToGetUser(null, request.username());

        assertThatThrownBy(() -> target.authenticate(request)).isInstanceOf(UserNotFoundException.class);

        verifyNoInteractions(tokenService);
        verifyNoInteractions(passwordService);
    }

    @Test
    void authenticate_PasswordDoesNotMatch_InvalidCredentialsException() {
        var request = Data.AUTHENTICATION_REQUEST;
        var user    = SecurityFixtures.USER;

        whenNeedToGetUser(user, request.username());
        whenNeedToCheckPasswordMatch(request.password(), user.password(), false);

        assertThatThrownBy(() -> target.authenticate(request)).isInstanceOf(InvalidCredentialsException.class);

        verifyNoInteractions(tokenService);
    }

    @Test
    void authenticate_UserFoundAndPasswordMatch_IssueToken() {
        var request = Data.AUTHENTICATION_REQUEST;
        var user    = SecurityFixtures.USER;
        var token   = SecurityFixtures.TOKEN;

        whenNeedToGetUser(user, request.username());
        whenNeedToCheckPasswordMatch(request.password(), user.password(), true);
        whenNeedToIssueToken(new UserTokenInfo(user.username()), token);

        var actual = target.authenticate(request);

        assertThat(actual)
                .extracting(AuthenticationResponse::token)
                .isEqualTo(token);
    }

}
