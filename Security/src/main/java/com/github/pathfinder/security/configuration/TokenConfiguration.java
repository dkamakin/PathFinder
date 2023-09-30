package com.github.pathfinder.security.configuration;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@ToString
@RefreshScope
@Configuration
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TokenConfiguration {

    @UtilityClass
    public static class Token {

        public static final String SECRET                 = "${token.secret}";
        public static final String ACCESS_TOKEN_LIFETIME  = "${token.access.lifetime:PT30S}";
        public static final String REFRESH_TOKEN_LIFETIME = "${token.refresh.lifetime:PT5M}";
    }

    @ToString.Exclude
    @Value(Token.SECRET)
    private String   secret;
    @Value(Token.ACCESS_TOKEN_LIFETIME)
    private Duration accessTokenLifetime;
    @Value(Token.REFRESH_TOKEN_LIFETIME)
    private Duration refreshTokenLifetime;

}
