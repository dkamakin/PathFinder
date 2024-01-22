package com.github.pathfinder.security.listener;

import com.github.pathfinder.core.exception.ErrorCode;
import com.github.pathfinder.core.exception.ServiceException;
import com.github.pathfinder.security.api.SecurityApi;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.data.UserInfo;
import com.github.pathfinder.security.configuration.SecurityAmqpTest;
import com.github.pathfinder.security.database.entity.UserEntity;
import com.github.pathfinder.security.service.IUserService;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import static com.github.pathfinder.security.SecurityFixtures.TOKEN;
import static com.github.pathfinder.security.SecurityFixtures.USER_ENTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SecurityAmqpTest
class SecurityListenerTest {

    @Autowired
    SecurityApi securityApi;

    @MockBean
    IUserService userService;

    void whenNeedToGetUserInfo(Token token, UserEntity expected) {
        when(userService.read(token)).thenReturn(Optional.ofNullable(expected));
    }

    static Stream<String> invalidGetUserInfoRequests() {
        return Stream.of(
                "",
                "     "
        );
    }

    @ParameterizedTest
    @MethodSource("invalidGetUserInfoRequests")
    void handle_InvalidRequest_ServiceException(String request) {
        assertThatThrownBy(() -> securityApi.userInfo(request))
                .isInstanceOf(ServiceException.class)
                .satisfies(exception ->
                                   assertThat(((ServiceException) exception).errorCode())
                                           .isEqualTo(ErrorCode.BAD_REQUEST));
    }

    @Test
    void handle_ValidRequest_CallService() {
        var user     = USER_ENTITY;
        var expected = new UserInfo(user.getName(), user.getRoles().getRole());

        whenNeedToGetUserInfo(TOKEN, user);

        var actual = securityApi.userInfo(TOKEN.value());

        assertThat(actual).isEqualTo(expected);
    }

}
