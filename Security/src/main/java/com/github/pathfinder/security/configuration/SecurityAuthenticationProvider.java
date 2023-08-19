package com.github.pathfinder.security.configuration;

import com.github.pathfinder.security.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityAuthenticationProvider implements AuthenticationProvider {

    private final UserService     userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authenticating {}", authentication.getName());

        String providedRawPassword = authentication.getCredentials().toString();
        String userPassword        = userService.loadUserByUsername(authentication.getName()).getPassword();

        if (passwordEncoder.matches(providedRawPassword, userPassword)) {
            log.info("successfully authenticated");
            return new UsernamePasswordAuthenticationToken(
                    authentication.getName(),
                    authentication.getCredentials(),
                    authentication.getAuthorities());
        }

        log.info("failed to authenticate");
        throw new BadCredentialsException("Wrong credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
