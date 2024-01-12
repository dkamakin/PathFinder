package com.github.pathfinder.security.web.mapper;

import com.github.pathfinder.security.api.data.AuthenticationRequest;
import com.github.pathfinder.security.api.data.AuthenticationResponse;
import com.github.pathfinder.security.api.data.SessionRefreshRequest;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.web.dto.AuthenticationRequestDto;
import com.github.pathfinder.security.web.dto.AuthenticationResponseDto;
import com.github.pathfinder.security.web.dto.SessionRefreshRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DtoMapper {

    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    @Mapping(target = "accessToken", source = "accessToken.value")
    @Mapping(target = "refreshToken", source = "refreshToken.value")
    AuthenticationResponseDto authenticationResponse(AuthenticationResponse response);

    AuthenticationRequest authenticationRequest(AuthenticationRequestDto request);

    SessionRefreshRequest sessionRefreshRequest(SessionRefreshRequestDto request);

    @Mapping(target = "value", source = "value")
    Token token(String value);

    String string(Token value);

}
