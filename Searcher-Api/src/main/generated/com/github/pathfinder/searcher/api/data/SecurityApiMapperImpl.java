package com.github.pathfinder.searcher.api.data;

import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-24T20:46:33+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8.1 (Eclipse Adoptium)"
)
public class SecurityApiMapperImpl implements SecurityApiMapper {

    @Override
    public Token token(String value) {
        if ( value == null ) {
            return null;
        }

        String value1 = null;

        value1 = value;

        Token token = new Token( value1 );

        return token;
    }

    @Override
    public Tokens tokens(String accessToken, String refreshToken) {
        if ( accessToken == null && refreshToken == null ) {
            return null;
        }

        Token accessToken1 = null;
        accessToken1 = token( accessToken );
        Token refreshToken1 = null;
        refreshToken1 = token( refreshToken );

        Tokens tokens = new Tokens( accessToken1, refreshToken1 );

        return tokens;
    }

    @Override
    public AuthenticationResponse authenticationResponse(Tokens token) {
        if ( token == null ) {
            return null;
        }

        Token accessToken = null;
        Token refreshToken = null;

        accessToken = token.accessToken();
        refreshToken = token.refreshToken();

        AuthenticationResponse authenticationResponse = new AuthenticationResponse( accessToken, refreshToken );

        return authenticationResponse;
    }

    @Override
    public GetUserInfoRequest userInfoRequest(String token) {
        if ( token == null ) {
            return null;
        }

        Token token1 = null;

        token1 = token( token );

        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest( token1 );

        return getUserInfoRequest;
    }
}
