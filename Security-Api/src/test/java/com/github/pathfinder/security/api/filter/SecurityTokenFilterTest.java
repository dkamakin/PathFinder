package com.github.pathfinder.security.api.filter;

import com.github.pathfinder.core.configuration.CoreConfiguration;
import com.github.pathfinder.core.tools.impl.JsonTools;
import com.github.pathfinder.core.web.tools.FilterResponseWriter;
import com.github.pathfinder.security.api.SecurityApi;
import com.github.pathfinder.security.api.SecurityFixtures;
import com.github.pathfinder.security.api.data.UserInfo;
import com.github.pathfinder.security.api.header.SecurityHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        FilterResponseWriter.class,
        TokenSanitizer.class,
        SecurityTokenFilter.class,
        JsonTools.class,
        CoreConfiguration.class
})
class SecurityTokenFilterTest {

    @MockBean
    SecurityApi securityApi;

    @Autowired
    SecurityTokenFilter target;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletOutputStream outputStream;

    @Mock
    FilterChain filterChain;

    void whenNeedToGetToken(String expected) {
        when(request.getHeader(SecurityHeaders.AUTHORIZATION_TOKEN_HEADER)).thenReturn(expected);
    }

    void whenNeedToGetUser(String token, UserInfo expected) {
        when(securityApi.userInfo(token)).thenReturn(expected);
    }

    void whenNeedToThrowOnGetUser(String token) {
        doThrow(new RuntimeException()).when(securityApi).userInfo(token);
    }

    void whenNeedToGetOutputStream(HttpServletResponse response, ServletOutputStream expected) throws IOException {
        when(response.getOutputStream()).thenReturn(expected);
    }

    static Stream<String> emptyStrings() {
        return Stream.of(
                "",
                null,
                "    "
        );
    }

    @ParameterizedTest
    @MethodSource("emptyStrings")
    void doFilterInternal_TokenIsNotPresent_FilterWithNoAction(String header) throws ServletException, IOException {
        whenNeedToGetToken(header);

        target.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(response);
        verifyNoInteractions(securityApi);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternal_FailedToGetUserInfoFromToken_RejectRequest() throws ServletException, IOException {
        var token = SecurityFixtures.TOKEN;

        whenNeedToGetToken(token.value());
        whenNeedToThrowOnGetUser(token.value());
        whenNeedToGetOutputStream(response, outputStream);

        target.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternal_FetchedUserSuccessfully_ProcessRequest() throws ServletException, IOException {
        var token       = SecurityFixtures.TOKEN;
        var expected    = SecurityFixtures.USER_INFO;
        var tokenString = token.value();
        var authorities = List.of(new SimpleGrantedAuthority(expected.role()));

        whenNeedToGetToken(tokenString);
        whenNeedToGetUser(tokenString, expected);

        target.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(response);

        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .matches(authentication -> authentication.getCredentials().equals(tokenString))
                .matches(authentication -> authentication.getAuthorities().equals(authorities))
                .matches(authentication -> authentication.getPrincipal().equals(expected));
    }

    @Test
    void doFilterInternal_TokenContainsBearerPart_SanitizeTokenAndProcessRequest()
            throws ServletException, IOException {
        var token       = "accessToken";
        var bearerToken = "Bearer " + token;
        var expected    = SecurityFixtures.USER_INFO;
        var authorities = List.of(new SimpleGrantedAuthority(expected.role()));

        whenNeedToGetToken(bearerToken);
        whenNeedToGetUser(token, expected);

        target.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(response);

        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .matches(authentication -> authentication.getCredentials().equals(token))
                .matches(authentication -> authentication.getAuthorities().equals(authorities))
                .matches(authentication -> authentication.getPrincipal().equals(expected));
    }
}
