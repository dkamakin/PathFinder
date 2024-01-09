package com.github.pathfinder.security.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@ToString
@Validated
@RefreshScope
@Configuration
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TokenConfiguration {

    @NotBlank
    @ToString.Exclude
    @Value("${token.secret}")
    private String secret;

    @NotNull
    @Value("${token.access.lifetime:PT30S}")
    private Duration accessTokenLifetime;

    @NotNull
    @Value("${token.refresh.lifetime:PT5M}")
    private Duration refreshTokenLifetime;

}
