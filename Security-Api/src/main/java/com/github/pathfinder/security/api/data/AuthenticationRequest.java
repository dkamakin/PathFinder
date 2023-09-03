package com.github.pathfinder.security.api.data;

import com.google.common.base.MoreObjects;

public record AuthenticationRequest(String username, String password) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .toString();
    }
}
