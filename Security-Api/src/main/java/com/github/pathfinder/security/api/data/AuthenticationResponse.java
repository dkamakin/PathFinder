package com.github.pathfinder.security.api.data;

public record AuthenticationResponse(Token accessToken,
                                     Token refreshToken) {

}
