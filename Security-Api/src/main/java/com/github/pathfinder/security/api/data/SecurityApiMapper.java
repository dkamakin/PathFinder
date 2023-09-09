package com.github.pathfinder.security.api.data;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Mapper
public interface SecurityApiMapper {

    SecurityApiMapper INSTANCE = Mappers.getMapper(SecurityApiMapper.class);

    @Mapping(target = "value", source = "value")
    Token token(String value);

    Tokens tokens(String accessToken, String refreshToken);

    AuthenticationResponse authenticationResponse(Tokens token);

    @Mapping(target = "token", source = "token")
    GetUserInfoRequest userInfoRequest(String token);

    default List<GrantedAuthority> grantedAuthority(UserInfo userInfo) {
        return List.of(new SimpleGrantedAuthority(userInfo.role()));
    }

}
