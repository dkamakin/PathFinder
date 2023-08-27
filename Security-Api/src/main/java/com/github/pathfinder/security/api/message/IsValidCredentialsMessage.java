package com.github.pathfinder.security.api.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public record IsValidCredentialsMessage(@JsonProperty(Token.USERNAME)
                                        String username,
                                        @JsonProperty(Token.PASSWORD)
                                        String password) {

    private static final class Token {
        static final String USERNAME = "username";
        static final String PASSWORD = "password";
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add(Token.USERNAME, username)
                .toString();
    }
}
