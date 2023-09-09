package com.github.pathfinder.security.service.impl;

import com.auth0.jwt.JWT;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.security.SecurityFixtures;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.exception.InvalidTokenException;
import com.github.pathfinder.security.configuration.JwtTools;
import com.github.pathfinder.security.configuration.SecurityIntegrationTest;
import com.github.pathfinder.security.configuration.TokenConfiguration;
import com.github.pathfinder.security.data.jwt.JwtPayload;
import com.github.pathfinder.security.service.ITokenService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

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
        when(timeSupplier.instant()).thenReturn(expected);
    }

    void assertPayload(JwtPayload expected, Token... tokens) {
        Arrays.stream(tokens).forEach(token -> assertPayload(token, expected));
    }

    void assertPayload(Token token, JwtPayload expected) {
        var actual = target.payload(token);

        assertThat(actual)
                .matches(result -> result.userId().equals(expected.userId()))
                .matches(result -> result.device().equals(expected.device()));
    }

    @Test
    void issue_SameUserAndDevice_RefreshNotEqualsToAccess() {
        whenNeedToGetTime(Instant.now());

        var payload = SecurityFixtures.JWT_PAYLOAD;
        var tokens  = target.issue(payload);

        assertThat(tokens.accessToken().value()).isNotEqualTo(tokens.refreshToken().value());
    }

    @Test
    void payload_InvalidTokenProvided_InvalidTokenException() {
        var token = new Token("123");

        assertThatThrownBy(() -> target.payload(token)).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void payload_ExpiredTokenProvided_InvalidTokenException() {
        var now = Instant.now();

        whenNeedToGetTime(now);

        var payload             = SecurityFixtures.JWT_PAYLOAD;
        var tokens              = target.issue(payload);
        var accessToken         = tokens.accessToken();
        var refreshToken        = tokens.refreshToken();
        var decodedAccessToken  = JWT.require(jwtTools.getAlgorithm()).build().verify(accessToken.value());
        var decodedRefreshToken = JWT.require(jwtTools.getAlgorithm()).build().verify(refreshToken.value());

        assertThat(decodedRefreshToken.getIssuedAtAsInstant())
                .isCloseTo(now, within(1, ChronoUnit.SECONDS));
        assertThat(decodedAccessToken.getIssuedAtAsInstant())
                .isCloseTo(now, within(1, ChronoUnit.SECONDS));
        assertThat(decodedAccessToken.getExpiresAtAsInstant())
                .isCloseTo(now.plus(tokenConfiguration.getAccessTokenLifetime()), within(1, ChronoUnit.SECONDS));
        assertThat(decodedRefreshToken.getExpiresAtAsInstant())
                .isCloseTo(now.plus(tokenConfiguration.getRefreshTokenLifetime()), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void payload_TokenIsValid_ReturnUserInfo() {
        whenNeedToGetTime(Instant.now());

        var payload = SecurityFixtures.JWT_PAYLOAD;
        var token   = target.issue(payload);

        assertPayload(payload, token.accessToken(), token.refreshToken());
    }

}
