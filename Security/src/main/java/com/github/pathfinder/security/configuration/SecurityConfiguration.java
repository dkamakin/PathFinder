package com.github.pathfinder.security.configuration;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.core.web.configuration.impl.CoreWebConfiguration;
import com.github.pathfinder.security.api.configuration.SecurityApiConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RefreshScope
@Import({SecurityApiConfiguration.class, CoreConfiguration.class, CoreWebConfiguration.class})
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
