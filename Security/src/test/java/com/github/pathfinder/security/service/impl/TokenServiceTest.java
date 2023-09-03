package com.github.pathfinder.security.service.impl;

import com.auth0.jwt.JWT;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.exception.InvalidTokenException;
import com.github.pathfinder.security.configuration.JwtTools;
import com.github.pathfinder.security.configuration.SecurityIntegrationTest;
import com.github.pathfinder.security.configuration.TokenConfiguration;
import com.github.pathfinder.security.data.user.UserTokenInfo;
import com.github.pathfinder.security.service.ITokenService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.doReturn;

@SecurityIntegrationTest
@ContextConfiguration(classes = {JwtTools.class, TokenConfiguration.class, TokenService.class, JwtPayloadService.class})
class TokenServiceTest {

    @Autowired
    TokenConfiguration tokenConfiguration;

    @Autowired
    JwtTools jwtTools;

    @MockBean
    IDateTimeSupplier timeSupplier;

    @Autowired
    ITokenService target;

    void whenNeedToGetTime(Instant expected) {
        doReturn(expected).when(timeSupplier).instant();
    }

    @Test
    void issue_UserInfoProvided_TokenIssued() {
        whenNeedToGetTime(Instant.now());

        var actual = target.issue(new UserTokenInfo("some_name"));

        assertThat(actual).isNotNull();
    }

    @Test
    void userInfo_InvalidTokenProvided_InvalidTokenException() {
        var token = new Token("123");

        assertThatThrownBy(() -> target.userInfo(token)).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void userInfo_ExpiredTokenProvided_InvalidTokenException() {
        var now            = Instant.now();
        var lifetime       = tokenConfiguration.getLifeTime();
        var expirationTime = now.plus(lifetime);

        whenNeedToGetTime(now);

        var user    = new UserTokenInfo("user");
        var token   = target.issue(user);
        var decoded = JWT.require(jwtTools.getAlgorithm()).build().verify(token.value());

        assertThat(decoded.getIssuedAtAsInstant())
                .isCloseTo(now, within(1, ChronoUnit.SECONDS));
        assertThat(decoded.getExpiresAtAsInstant())
                .isCloseTo(expirationTime, within(1, ChronoUnit.SECONDS));
    }

    @Test
    void userInfo_TokenIsValid_ReturnUserInfo() {
        whenNeedToGetTime(Instant.now());

        var user   = new UserTokenInfo("user");
        var token  = target.issue(user);
        var actual = target.userInfo(token);

        assertThat(actual)
                .matches(result -> result.username().equals(user.username()));
    }
}
