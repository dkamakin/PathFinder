package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.security.SecurityFixtures;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.exception.UserAlreadyRegisteredException;
import com.github.pathfinder.security.configuration.SecurityServiceDatabaseTest;
import com.github.pathfinder.security.data.jwt.JwtPayload;
import com.github.pathfinder.security.service.ITokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SecurityServiceDatabaseTest
@Import({UserService.class, PasswordService.class, BCryptPasswordEncoder.class})
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordService passwordService;

    @MockBean
    ITokenService tokenService;

    void whenNeedToReadToken(Token token, JwtPayload expected) {
        when(tokenService.payload(token)).thenReturn(expected);
    }

    @Test
    void read_UserIsNotPresentInDatabase_EmptyResult() {
        var username = SecurityFixtures.USERNAME;

        assertThat(userService.read(username)).isEmpty();
    }

    @Test
    void read_UserIsPresentInDatabase_ReturnUser() {
        var saveUserRequest = SecurityFixtures.SAVE_USER_REQUEST;
        var expected        = saveUserRequest.username();

        assertThat(userService.read(expected)).isEmpty();

        userService.save(saveUserRequest);

        assertThat(userService.read(expected))
                .get()
                .matches(actual -> actual.getName().equals(expected));
    }

    @Test
    void read_TokenIssuedToExistingUser_ReturnUserInfo() {
        var saveUserRequest = SecurityFixtures.SAVE_USER_REQUEST;
        var expected        = saveUserRequest.username();
        var token           = SecurityFixtures.token();
        var saved           = userService.save(saveUserRequest);

        whenNeedToReadToken(token, new JwtPayload(saved.getId(), SecurityFixtures.DEVICE_INFO));

        assertThat(userService.read(token))
                .get()
                .matches(actual -> actual.getName().equals(expected));
    }

    @Test
    void read_TokenIssuedToNonExistingUser_Empty() {
        var token = SecurityFixtures.token();

        whenNeedToReadToken(token, new JwtPayload(1L, SecurityFixtures.DEVICE_INFO));

        assertThat(userService.read(token)).isEmpty();
    }

    @Test
    void save_UserAlreadyPresentInDatabase_UserAlreadyRegisteredException() {
        var saveUserRequest = SecurityFixtures.SAVE_USER_REQUEST;
        var expected        = saveUserRequest.username();

        assertThat(userService.read(expected)).isEmpty();

        var saved = userService.save(saveUserRequest);

        assertThat(saved)
                .matches(actual -> actual.getName().equals(saveUserRequest.username()))
                .matches(actual -> actual.getRoles().getRole().equals(saveUserRequest.role()))
                .matches(actual -> actual.getRefreshTokens().isEmpty())
                .matches(actual -> !actual.getPassword().equals(saveUserRequest.password()))
                .matches(actual -> passwordService.matches(saveUserRequest.password(), actual.getPassword()));

        assertThatThrownBy(() -> userService.save(saveUserRequest)).isInstanceOf(UserAlreadyRegisteredException.class);

        assertThat(userService.read(expected))
                .get()
                .matches(actual -> actual.getName().equals(expected));
    }

}
