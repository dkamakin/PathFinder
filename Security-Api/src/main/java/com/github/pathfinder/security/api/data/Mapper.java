package com.github.pathfinder.security.api.data;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class Mapper {

    public static Token token(String token) {
        return new Token(token);
    }

    public static AuthenticationResponse authenticationResponse(Token token) {
        return new AuthenticationResponse(token);
    }

    public static GetUserInfoRequest userInfoRequest(String token) {
        return new GetUserInfoRequest(token(token));
    }

    public static List<GrantedAuthority> grantedAuthority(UserInfo userInfo) {
        return List.of(new SimpleGrantedAuthority(userInfo.role()));
    }

}
