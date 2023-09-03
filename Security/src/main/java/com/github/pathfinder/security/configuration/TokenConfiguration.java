package com.github.pathfinder.security.configuration;

import com.google.common.base.MoreObjects;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@RefreshScope
@Configuration
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TokenConfiguration {

    public static final class Token {

        public static final String SECRET   = "${token.secret}";
        public static final String LIFETIME = "${token.lifetime:PT30S}";

    }

    @Value(Token.SECRET)
    private String secret;

    @Value(Token.LIFETIME)
    private Duration lifeTime;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("secret", "***")
                .add("lifeTime", lifeTime)
                .toString();
    }
}
