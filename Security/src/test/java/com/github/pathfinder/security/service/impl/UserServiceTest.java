package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.security.SecurityFixtures;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.exception.UserAlreadyRegisteredException;
import com.github.pathfinder.security.configuration.SecurityServiceDatabaseTest;
import com.github.pathfinder.security.configuration.SecurityTestDatabaseTemplate;
import com.github.pathfinder.security.data.user.UserTokenInfo;
import com.github.pathfinder.security.service.ITokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SecurityServiceDatabaseTest
@Import(UserService.class)
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    ITokenService tokenService;

    @Autowired
    SecurityTestDatabaseTemplate securityTestDatabaseTemplate;

    @BeforeEach
    void setUp() {
        securityTestDatabaseTemplate.cleanDatabase();
    }

    void whenNeedToReadToken(Token token, UserTokenInfo expected) {
        doReturn(expected).when(tokenService).userInfo(token);
    }

    @Test
    void read_UserIsNotPresentInDatabase_EmptyResult() {
        var username = SecurityFixtures.USERNAME;

        assertThat(userService.read(username)).isEmpty();
    }

    @Test
    void read_UserIsPresentInDatabase_ReturnUser() {
        var user     = SecurityFixtures.USER;
        var expected = user.username();

        assertThat(userService.read(expected)).isEmpty();

        userService.save(user);

        assertThat(userService.read(expected))
                .get()
                .matches(actual -> actual.username().equals(expected));
    }

    @Test
    void read_TokenIssuedToExistingUser_ReturnUserInfo() {
        var user     = SecurityFixtures.USER;
        var expected = user.username();
        var token    = SecurityFixtures.TOKEN;

        whenNeedToReadToken(token, new UserTokenInfo(user.username()));

        assertThat(userService.read(token)).isEmpty();

        userService.save(user);

        assertThat(userService.read(token))
                .get()
                .matches(actual -> actual.username().equals(expected));

        verify(tokenService, times(2)).userInfo(token);
    }

    @Test
    void save_UserAlreadyPresentInDatabase_UserAlreadyRegisteredException() {
        var user     = SecurityFixtures.USER;
        var expected = user.username();

        assertThat(userService.read(expected)).isEmpty();

        userService.save(user);

        assertThatThrownBy(() -> userService.save(user)).isInstanceOf(UserAlreadyRegisteredException.class);

        assertThat(userService.read(expected))
                .get()
                .matches(actual -> actual.username().equals(expected));
    }

}
