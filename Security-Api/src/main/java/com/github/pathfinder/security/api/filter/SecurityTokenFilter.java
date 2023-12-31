package com.github.pathfinder.security.api.filter;

import com.github.pathfinder.core.exception.ErrorMessage;
import com.github.pathfinder.core.web.tools.FilterResponseWriter;
import com.github.pathfinder.security.api.SecurityApi;
import com.github.pathfinder.security.api.data.SecurityApiMapper;
import com.github.pathfinder.security.api.data.UserInfo;
import com.github.pathfinder.security.api.header.SecurityHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityTokenFilter extends OncePerRequestFilter {

    private final SecurityApi          securityApi;
    private final FilterResponseWriter responseWriter;
    private final TokenSanitizer       tokenSanitizer;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        var token = request.getHeader(SecurityHeaders.AUTHORIZATION_TOKEN_HEADER);

        if (StringUtils.isBlank(token)) {
            log.debug("Authentication token not found");
            filterChain.doFilter(request, response);
            return;
        }

        var sanitizedToken = tokenSanitizer.sanitize(token);
        var userInfo       = userInfo(sanitizedToken);

        if (userInfo.isPresent()) {
            authenticate(sanitizedToken, userInfo.get(), request);
            filterChain.doFilter(request, response);
        } else {
            sendUnauthorized(response);
        }
    }

    private void authenticate(String token, UserInfo userInfo, HttpServletRequest request) {
        log.debug("The token is valid, authenticating");

        var authorities         = SecurityApiMapper.INSTANCE.grantedAuthority(userInfo);
        var authenticationToken = new UsernamePasswordAuthenticationToken(userInfo, token, authorities);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private Optional<UserInfo> userInfo(String token) {
        try {
            return Optional.of(securityApi.userInfo(token));
        } catch (Exception e) {
            log.error("Failed to check the token validity: {}", e.getMessage());
        }

        return Optional.empty();
    }

    private void sendUnauthorized(HttpServletResponse response) {
        log.debug("Failed to authenticate");

        responseWriter
                .to(response)
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .write(new ErrorMessage("Provided access token is invalid"));
    }

}
