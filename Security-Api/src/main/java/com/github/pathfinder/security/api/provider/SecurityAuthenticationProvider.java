package com.github.pathfinder.security.api.provider;

import com.github.pathfinder.security.api.messaging.SecurityApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * TODO: move token cache to the security microservice
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityAuthenticationProvider implements AuthenticationProvider {

    private final SecurityApi securityApi;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authenticating {}", authentication.getName());

        if (securityApi.isValid(authentication.getName(), authentication.getCredentials().toString())) {
            log.info("successfully authenticated");
            return new UsernamePasswordAuthenticationToken(
                    authentication.getName(),
                    authentication.getCredentials(),
                    authentication.getAuthorities()
            );
        }

        log.info("failed to authenticate");
        throw new BadCredentialsException("Wrong credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
