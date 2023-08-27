package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.security.UserFixtures;
import com.github.pathfinder.security.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    IUserService userService;

    @InjectMocks
    SecurityService target;

    void whenNeedToGetUser(String username, UserDetails user) {
        doReturn(user).when(userService).loadUserByUsername(username);
    }

    void whenNeedToCheckPassowrdEquals(String rawProvidedPassowrd, String userPassword, boolean expected) {
        doReturn(expected).when(passwordEncoder).matches(rawProvidedPassowrd, userPassword);
    }

    @Test
    void isValid_PasswordNotValid_False() {
        var username = UserFixtures.USERNAME;
        var password = UserFixtures.PASSWORD;
        var user     = UserFixtures.USER_DETAILS;
        var expected = false;

        whenNeedToGetUser(username, user);
        whenNeedToCheckPassowrdEquals(password, user.getPassword(), expected);

        var actual = target.isValid(username, password);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void isValid_PasswordValid_True() {
        var username = UserFixtures.USERNAME;
        var password = UserFixtures.PASSWORD;
        var user     = UserFixtures.USER_DETAILS;
        var expected = true;

        whenNeedToGetUser(username, user);
        whenNeedToCheckPassowrdEquals(password, user.getPassword(), expected);

        var actual = target.isValid(username, password);

        assertThat(actual).isEqualTo(expected);
    }

}
