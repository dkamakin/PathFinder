package com.github.pathfinder.security.configuration;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.security.api.configuration.SecurityApiConfiguration;
import com.github.pathfinder.security.service.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@Import({SecurityApiConfiguration.class, CoreConfiguration.class})
public class SecurityConfiguration {

    @Bean
    public UserDetailsManager userDetailsService(IUserService userService) {
        return userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
