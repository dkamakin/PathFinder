package com.github.pathfinder.security.service.impl;

import com.github.pathfinder.security.UserFixtures;
import com.github.pathfinder.security.configuration.SecurityServiceDatabaseTest;
import com.github.pathfinder.security.configuration.SecurityTestDatabaseTemplate;
import com.github.pathfinder.security.exception.UserAlreadyRegisteredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SecurityServiceDatabaseTest
@Import(UserService.class)
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    SecurityTestDatabaseTemplate securityTestDatabaseTemplate;

    @BeforeEach
    void setUp() {
        securityTestDatabaseTemplate.cleanDatabase();
    }

    @Test
    void create_UserNotPresent_SaveUserEntity() {
        var user = UserFixtures.USER_DETAILS;

        assertThat(userService.userExists(user.getUsername())).isFalse();

        userService.createUser(user);

        assertThat(userService.userExists(user.getUsername())).isTrue();
    }

    @Test
    void createUser_UserPresent_UserAlreadyRegisteredException() {
        var user = UserFixtures.USER_DETAILS;

        userService.createUser(user);

        assertThat(userService.userExists(user.getUsername())).isTrue();

        assertThatThrownBy(() -> userService.createUser(user)).isInstanceOf(UserAlreadyRegisteredException.class);
    }

    @Test
    void loadUserByUsername_UserNotPresent_UsernameNotFoundException() {
        var user     = UserFixtures.USER_DETAILS;
        var username = user.getUsername();

        assertThat(userService.userExists(user.getUsername())).isFalse();

        assertThatThrownBy(() -> userService.loadUserByUsername(username)).isInstanceOf(
                UsernameNotFoundException.class);
    }

    @Test
    void loadUserByUsername_UserPresent_ReturnUserDetails() {
        var user = UserFixtures.USER_DETAILS;

        userService.createUser(user);

        var actual = userService.loadUserByUsername(user.getUsername());

        assertThat(actual).isEqualTo(user);
    }

    @Test
    void updatePassword_UserNotPresent_UsernameNotFoundException() {
        var user = UserFixtures.USER_DETAILS;

        assertThatThrownBy(() -> userService.updatePassword(user, "newPassword"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void updatePassword_UserPresent_ReturnUserDetails() {
        var user        = UserFixtures.USER_DETAILS;
        var newPassword = "newPassword";

        userService.createUser(user);

        var actual = userService.updatePassword(user, newPassword);

        assertThat(actual).extracting(UserDetails::getPassword).isEqualTo(newPassword);
    }

}
