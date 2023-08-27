package com.github.pathfinder.security.api;

import com.github.pathfinder.security.api.messaging.SecurityApi;
import com.github.pathfinder.security.api.provider.SecurityAuthenticationProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SecurityAuthenticationProviderTest {

    @Mock
    SecurityApi securityApi;

    @InjectMocks
    SecurityAuthenticationProvider target;

    void whenNeedToCheckIsValid(String username, String password, boolean result) {
        doReturn(result).when(securityApi).isValid(username, password);
    }

    @Test
    void authenticate_CredentialsValid_ReturnAuthentication() {
        var request = SecurityFixtures.AUTHENTICATION;

        whenNeedToCheckIsValid(request.getName(), request.getCredentials().toString(), true);

        var actual = target.authenticate(request);

        assertThat(actual)
                .matches(result -> result.getName().equals(request.getName()))
                .matches(result -> result.getCredentials().equals(request.getCredentials()));
    }

    @Test
    void authenticate_CredentialsInvalid_BadCredentialsException() {
        var request = SecurityFixtures.AUTHENTICATION;

        whenNeedToCheckIsValid(request.getName(), request.getCredentials().toString(), false);

        assertThatThrownBy(() -> target.authenticate(request)).isInstanceOf(BadCredentialsException.class);
    }
}
